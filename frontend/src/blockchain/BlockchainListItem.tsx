import {CustomLink} from '../common/CustomLink'
import {Divider, ListItem, ListItemIcon, ListItemText, Zoom} from '@material-ui/core'
import {shorterHash} from '../utils'
import React, {memo} from 'react'
import {Stop as StopIcon} from '@material-ui/icons'

export const BlockChainListItem = memo<any>(({item, style}) => {
    const date = new Date(item.timestamp)
    const primaryText = `Block ${item.index} [${(item.hash && shorterHash(item.hash)) ?? 'Genesis'}]`
    const secondaryText = `(${date.toLocaleString()})`
    return (
        <ListItem component={CustomLink} to={`/blockchain/${item.index}`} button style={style} pb={1}>
            {/*{console.log('BlockChainListItem', item, date)}*/}
            <ListItemIcon>
                <StopIcon/>
            </ListItemIcon>
            <ListItemText primary={primaryText} secondary={secondaryText}/>
        </ListItem>
    )
})
