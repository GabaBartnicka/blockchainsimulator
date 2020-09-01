import React, {useContext, useState} from 'react'
import {List, ListItem, ListItemText, Typography} from '@material-ui/core'
import _ from 'lodash'
import {FixedSizeList} from 'react-window'
import InfiniteLoader from 'react-window-infinite-loader'
import {BlockChainListItem} from './BlockchainListItem'
import {BlockchainContext} from './BlockchainContext'

const PAGE_SIZE = 50
const START_POSITION = 900

export const BlockchainList = () => {
    const {blockchainList, loadBlockchainList} = useContext(BlockchainContext)
    const [isNextPageLoading, setNextPageLoading] = useState(false)

    const loadNextPage = (props) => {
        console.log('fetch', props)
        setNextPageLoading(true)
        const from = _.last(blockchainList)?.index - 1 - PAGE_SIZE
        const to = _.last(blockchainList)?.index - 1
        const result = loadBlockchainList(from, to)
        setNextPageLoading(false)
        return result
    }

    const hasNextPage = !!_.last(blockchainList)?.hash || !blockchainList.length
    const isItemLoaded = (index) => index < blockchainList.length
    const loadMoreItems = isNextPageLoading ? () => {
    } : loadNextPage
    const itemCount = hasNextPage ? blockchainList.length + 1 : blockchainList.length

    return (
        <>
            <InfiniteLoader isItemLoaded={isItemLoaded} itemCount={itemCount} loadMoreItems={loadMoreItems}>
                {({onItemsRendered, ref}) => (
                    <FixedSizeList
                        itemCount={itemCount}
                        onItemsRendered={onItemsRendered}
                        height={600}
                        width={'100%'}
                        itemSize={60}
                        innerElementType={List}
                        ref={ref}
                    >
                        {({index, style}) =>
                            isItemLoaded(index) ? (
                                <BlockChainListItem item={blockchainList[index]} style={style}/>
                            ) : null
                        }
                    </FixedSizeList>
                )}
            </InfiniteLoader>
        </>
    )
}
