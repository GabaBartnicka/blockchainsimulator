import React, {useEffect, useMemo, useState} from 'react'
import {Box, CircularProgress, Grid, Paper, Typography} from '@material-ui/core'
import {API} from '../api'
import {NodesGraph} from './NodesGraph'
import {NewNodeDialog} from './NewNodeDialog'

export const NodesPage = () => {
    const [peers, setPeers] = useState(null)

    useEffect(() => {
        API.get('v0/peers/').then((response) => {
            setPeers(response.data)
        })
    }, [])

    const graphData = useMemo(() => {
        const data = {
            nodes: [],
            edges: [
                {from: 1, to: 2},
                {from: 1, to: 3},
            ],
        }
        if (peers) {
            data.nodes.push(
                ...peers?.map((node, index) => ({id: index, label: node.name, title: `${node.host}:${node.port}`})),
            )
            data.nodes.forEach((node) => {
                console.log(node)
                data.edges.push({from: node.id, to: node.id + 1})
            })
        }
        return peers ? data : {}
    }, [peers])

    return (
        <>
            <Grid container spacing={1}>
                <Grid item xs={12}>
                    <Paper>
                        <Box p={2}>
                            <Typography variant={'h5'}>Nodes</Typography>
                        </Box>
                    </Paper>
                </Grid>
                <Grid item xs={12}>
                    <Paper>{peers ? <NodesGraph data={graphData}/> : <CircularProgress/>}</Paper>
                </Grid>
            </Grid>
            <NewNodeDialog/>
        </>
    )
}
