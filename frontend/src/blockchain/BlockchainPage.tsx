import React from 'react'
import {BlockCard} from './BlockCard'
import {Route, Switch} from 'react-router-dom'
import {BlockchainList} from './BlockchainList'
import {Box, Grid, Paper, Typography} from '@material-ui/core'
import {makeStyles} from '@material-ui/core/styles'
import {BlockchainContextProvider} from './BlockchainContext'

const useStyles = makeStyles((theme) => ({
    paper: {
        padding: theme.spacing(2),
        display: 'flex',
        overflow: 'auto',
        flexDirection: 'column',
    },
    fullHeight: {
        height: '100%',
    },
}))

export const BlockchainPage = () => {
    const classes = useStyles()
    return (
        <BlockchainContextProvider>
            <Grid container spacing={1}>
                <Grid item xs={12}>
                    <Paper>
                        <Box p={2}>
                            <Typography variant={'h5'}>Blockchain</Typography>
                        </Box>
                    </Paper>
                </Grid>

                <Grid item xs={12} md={6} lg={4}>
                    <Paper>
                        <BlockchainList/>
                    </Paper>
                </Grid>
                <Grid item xs={12} md={6} lg={8}>
                    <Switch>
                        <Route path="/blockchain/:blockId">
                            <BlockCard/>
                        </Route>
                        <Route>
                            <Paper>
                                <Box p={2}>
                                    <Typography variant={'h5'}>No block selected...</Typography>
                                </Box>
                            </Paper>
                        </Route>
                    </Switch>
                </Grid>
            </Grid>
        </BlockchainContextProvider>
    )
}
