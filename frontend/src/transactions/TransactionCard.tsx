import React, {FC} from 'react'
import {ChevronRight as ChevronRightIcon, ExpandMore as ExpandMoreIcon} from '@material-ui/icons'
import {TreeItem, TreeView} from '@material-ui/lab'
import {useParams} from 'react-router'
import {Card, CardContent, CircularProgress, Typography} from '@material-ui/core'
import _ from 'lodash'

export const TransactionCard: FC<any> = (props) => {
    const {transactionId} = useParams()
    const {transactions} = props
    const transaction = _.find(transactions, {id: transactionId})

    return transaction ? (
        <>
            <Card>
                <CardContent>
                    <Typography variant="h5" component="h2">
                        Transaction ID: {transaction.id}
                    </Typography>
                    <TreeView
                        defaultExpanded={['1', '2', '3', '4', '7']}
                        defaultCollapseIcon={<ExpandMoreIcon/>}
                        defaultExpandIcon={<ChevronRightIcon/>}
                    >
                        <TreeItem nodeId="1" label="Input">
                            <TreeItem nodeId="2" label={'Timestamp'}>
                                <Typography>{transaction.input.timestamp}</Typography>
                            </TreeItem>
                            <TreeItem nodeId="3" label="Amount">
                                <Typography>{transaction.input.amount}</Typography>
                            </TreeItem>
                            <TreeItem nodeId="4" label="Sender Address">
                                <TreeItem nodeId="5" label={transaction.input.senderAddress.label ?? '---'}>
                                    <Typography>{transaction.input.senderAddress.encoded}</Typography>
                                </TreeItem>
                            </TreeItem>
                            <TreeItem nodeId="6" label="Signature">
                                <Typography>{transaction.input.signature}</Typography>
                            </TreeItem>
                        </TreeItem>
                        <TreeItem nodeId="7" label="Outputs">
                            {transaction.outputs.map((output, index) => (
                                <TreeItem nodeId={800 + index} label={index + 1} key={800 + index}>
                                    <TreeItem nodeId={900 + index} label="Address">
                                        <TreeItem nodeId={1000 + index} label={output.address.label ?? '---'}>
                                            <Typography>{output.address.name}</Typography>
                                        </TreeItem>
                                    </TreeItem>
                                    <TreeItem nodeId={1100 + index} label="Amount">
                                        <Typography>{output.deltaAmount}</Typography>
                                    </TreeItem>
                                </TreeItem>
                            ))}
                        </TreeItem>
                    </TreeView>
                </CardContent>
            </Card>
        </>
    ) : (
        <CircularProgress/>
    )
}
