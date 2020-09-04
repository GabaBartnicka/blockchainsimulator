import {useParams} from 'react-router-dom'
import React, {useEffect, useState} from 'react'
import {
    Typography,
    LinearProgress,
    Card,
    CardContent,
    ExpansionPanel,
    ExpansionPanelSummary,
    ExpansionPanelDetails,
    Box,
    CircularProgress,
    Paper,
} from '@material-ui/core'
import {BlockCardTransactions} from './BlockCardTransactions'
import {API} from '../api'

export const BlockCard = () => {
    const {blockId} = useParams()
    const [block, setBlock] = useState()
    const [error, setError] = useState(null)

    useEffect(() => {
        setBlock(null)
        setError(false)
        API.get(`v0/block/${blockId}`)
            .then((response) => {
                setBlock(response.data)
            })
            .catch((reason) => {
                setError(true)
            })
    }, [blockId, setBlock])

    if (error) {
        return <Typography>Somethings went wrong</Typography>
    }

    return block ? (
        <Card>
            <CardContent>
                <Typography variant="h5" component="h2">
                    BlockID: {block.index}
                </Typography>
                <Typography color="textSecondary" gutterBottom>
                    Hash: {block.hash}
                </Typography>
                <Typography color="textSecondary" gutterBottom>
                    Mined at: {new Date(block.timestamp).toLocaleString()}
                </Typography>
                <Typography variant="body2" component="p">
                    {block.data?.message}
                </Typography>
                <BlockCardTransactions block={block}/>
            </CardContent>
        </Card>
    ) : (
        <Paper>
            <Box p={5} textAlign={'center'}>
                <CircularProgress/>
            </Box>
        </Paper>
    )
}
