import React, {FC} from 'react'
import {Add as AddIcon} from '@material-ui/icons'
import {createStyles, Fab, List, Paper} from '@material-ui/core'
import {makeStyles} from '@material-ui/core/styles'
import {BlockChainListItem} from '../blockchain/BlockchainListItem'
import {FixedSizeList} from 'react-window'
import {TransactionListItem} from './TransactionListItem'

export const TransactionList: FC<any> = (props) => {
    const {transactions} = props
    return (
        <>
            <FixedSizeList
                itemCount={transactions?.length}
                height={600}
                width={'100%'}
                itemSize={60}
                innerElementType={List}
            >
                {({index, style}) => <TransactionListItem item={transactions[index]} style={style}/>}
            </FixedSizeList>
        </>
    )
}
