import React from 'react'
import {
    Box,
    ExpansionPanel,
    ExpansionPanelDetails,
    ExpansionPanelSummary,
    TextField,
    Typography,
} from '@material-ui/core'
import {ExpandMore as ExpandMoreIcon} from '@material-ui/icons'
import {makeStyles} from '@material-ui/core/styles'
import ReactJson from 'react-json-view'

const useStyles = makeStyles((theme) => ({
    root: {
        width: '100%',
    },
    heading: {
        fontSize: theme.typography.pxToRem(15),
        flexBasis: '66.66%',
        flexShrink: 0,
    },
    secondaryHeading: {
        fontSize: theme.typography.pxToRem(15),
        color: theme.palette.text.secondary,
    },
    jsonPrint: {
        fontFamily: 'monospace',
        fontSize: '11pt',
    },
}))

export const BlockCardTransactions = (props) => {
    const classes = useStyles()
    const {block} = props
    const [expanded, setExpanded] = React.useState<string | false>(false)

    const handleChange = (panel: string) => (event: React.ChangeEvent<{}>, isExpanded: boolean) => {
        setExpanded(isExpanded ? panel : false)
    }

    return (
        <>
            <Typography>Transactions</Typography>
            {block?.data?.transactions?.map((transaction) => (
                <ExpansionPanel expanded={expanded === transaction.id} onChange={handleChange(transaction.id)}>
                    <ExpansionPanelSummary
                        expandIcon={<ExpandMoreIcon/>}
                        aria-controls="panel1bh-content"
                        id="panel1bh-header"
                    >
                        <Typography className={classes.heading}>{transaction.id}</Typography>
                        <Typography className={classes.secondaryHeading}>{transaction.valid && 'Valid'}</Typography>
                    </ExpansionPanelSummary>
                    <ExpansionPanelDetails>
                        <TransactionDetails transaction={transaction}/>
                    </ExpansionPanelDetails>
                </ExpansionPanel>
            ))}
        </>
    )
}

const TransactionDetails = ({transaction}) => {
    const classes = useStyles()
    return (
        <Box flexGrow={1}>
            {transaction.input && (
                <Box p={1}>
                    <TextField
                        disabled
                        label="Input"
                        multiline
                        variant={'outlined'}
                        fullWidth
                        InputProps={{className: classes.jsonPrint}}
                        value={JSON.stringify(transaction.input, null, 2)}
                    />
                </Box>
            )}
            <br/>
            {transaction.outputs && (
                <Box p={1}>
                    <TextField
                        disabled
                        label="Outputs"
                        multiline
                        variant={'outlined'}
                        fullWidth
                        InputProps={{className: classes.jsonPrint}}
                        value={JSON.stringify(transaction.outputs, null, 2)}
                    />
                </Box>
            )}
        </Box>
    )
}
