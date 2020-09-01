import React, {FC} from 'react'
import {
    ExpansionPanel,
    ExpansionPanelDetails,
    ExpansionPanelSummary,
    ListItem,
    ListItemIcon,
    ListItemText,
    Typography,
} from '@material-ui/core'
import {CustomLink} from '../common/CustomLink'
import {Stop as StopIcon} from '@material-ui/icons'

export const TransactionListItem: FC<any> = (props) => {
    const {item, style} = props
    console.log(item)
    return (
        <>
            <ListItem component={CustomLink} to={`/transactions/${item.id}`} button style={style} pb={1}>
                <ListItemIcon>
                    <StopIcon/>
                </ListItemIcon>
                <ListItemText primary={item.id} secondary={''}/>
            </ListItem>
        </>
    )
}
