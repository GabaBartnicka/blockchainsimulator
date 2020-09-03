import {Box, Button, CircularProgress, Grid, Paper, Typography} from '@material-ui/core'
import React, {useEffect, useMemo, useState} from 'react'
import {TransactionList} from './TransactionList'
import {NewTransactionDialog} from './NewTransactionDialog'
import {API, getEventStream, miningStatus, useWebSocket} from '../api'
import {useSnackbar} from 'notistack'
import {Route, Switch} from 'react-router'
import {TransactionCard} from './TransactionCard'

export const TransactionPage = () => {
    const [transactions, setTransactions] = useState<any>(null)
    const [loading, setLoading] = useState<any>(false)
    const [mining, setMining] = useState<any>(false)
    const snackbar = useSnackbar()

    const handleMine = () => {
        API.post('v0/mine/').then((response) => {
            getTransactionsFromApi()
        })
    }

    const getTransactionsFromApi = () => {
        setLoading(true)
        return API.get('v0/transactions/')
            .then((response) => {
                setTransactions(response.data)
            })
            .catch((error) => {
                snackbar.enqueueSnackbar(error.toString(), {
                    variant: 'error',
                })
            })
            .finally(() => {
                setLoading(false)
            })
    }

    useEffect(() => {
        getTransactionsFromApi()

        const transactionStream = getEventStream('transaction')
        transactionStream.onmessage = (message) => {
            getTransactionsFromApi().then(() => {
                snackbar.enqueueSnackbar('New Transaction', {
                    variant: 'info',
                })
            })
        }

        const miningStream = getEventStream('mining')
        miningStream.onmessage = (message) => {
            setMining(message?.data?.busy)
        }
        // miningStatus((response) => setMining(response?.data?.working))
    }, [])

    return (
        <>
            <Grid container spacing={1}>
                <Grid item xs={12}>
                    <Paper>
                        <Box p={2} display="flex" justifyContent={'space-between'}>
                            <Typography variant={'h5'}>Transactions</Typography>
                            <Button
                                variant="contained"
                                size="medium"
                                onClick={handleMine}
                                disabled={mining || !transactions?.length}
                            >
                                Mine
                            </Button>
                        </Box>
                    </Paper>
                </Grid>
                <Grid item xs={12} md={6} lg={4}>
                    <Paper>
                        {loading ? (
                            <CircularProgress/>
                        ) : transactions?.length ? (
                            <TransactionList transactions={transactions}/>
                        ) : (
                            <Box p={2}>
                                <Typography variant={'h5'}>Empty...</Typography>
                            </Box>
                        )}
                    </Paper>
                </Grid>
                <Grid item xs={12} md={6} lg={8}>
                    <Switch>
                        <Route path="/transactions/:transactionId">
                            <TransactionCard transactions={transactions}/>
                        </Route>
                        <Route>
                            <Paper>
                                <Box p={2}>
                                    <Typography variant={'h5'}>No transaction selected...</Typography>
                                </Box>
                            </Paper>
                        </Route>
                    </Switch>
                </Grid>
            </Grid>

            <NewTransactionDialog/>
        </>
    )
}
