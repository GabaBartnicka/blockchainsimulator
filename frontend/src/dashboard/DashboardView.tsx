import {Box, CircularProgress, Container, Grid, Paper, Typography} from '@material-ui/core'
import React, {useEffect, useState} from 'react'
import {API} from '../api'

export const DashboardView = () => {
    const [blockchainInfo, setBlockchainInfo] = useState(null)

    useEffect(() => {
        API.get('v0/blockchain/').then((response) => {
            setBlockchainInfo(response.data)
        })
    }, [])

    return (
        <Grid container spacing={1}>
            <Grid item xs={12}>
                <Paper>
                    <Box p={2}>
                        {blockchainInfo ? (
                            <>
                                <Typography variant={'h6'}>Wallet Public Address</Typography>
                                <Typography variant={'overline'}>
                                    {blockchainInfo.wallet?.publicAddress?.encoded}
                                </Typography>
                            </>
                        ) : (
                            <CircularProgress/>
                        )}
                    </Box>
                </Paper>
            </Grid>
            <Grid item xs={6} md={4}>
                <Paper>
                    <Box p={2} textAlign={'center'}>
                        {blockchainInfo ? (
                            <>
                                <Typography variant={'h6'}>Balance</Typography>
                                <Typography variant={'h4'}>{blockchainInfo.currentWalletAmount?.toFixed(2)}</Typography>
                            </>
                        ) : (
                            <CircularProgress/>
                        )}
                    </Box>
                </Paper>
            </Grid>
            <Grid item xs={6} md={4}>
                <Paper>
                    <Box p={2} textAlign={'center'}>
                        {blockchainInfo ? (
                            <>
                                <Typography variant={'h6'}>Blockchain length</Typography>
                                <Typography variant={'h4'}>{blockchainInfo.chains}</Typography>
                            </>
                        ) : (
                            <CircularProgress/>
                        )}
                    </Box>
                </Paper>
            </Grid>
            <Grid item xs={6} md={4} lg={4}>
                <Paper>
                    <Box p={2} textAlign={'center'}>
                        {blockchainInfo ? (
                            <>
                                <Typography variant={'h6'}>Blockchain difficulty</Typography>
                                <Typography variant={'h4'}>{blockchainInfo.difficulty}</Typography>
                            </>
                        ) : (
                            <CircularProgress/>
                        )}
                    </Box>
                </Paper>
            </Grid>
        </Grid>
    )
}
