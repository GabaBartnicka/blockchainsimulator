import React, {createContext, useEffect, useMemo, useState} from 'react'
import _ from 'lodash'
import {useSnackbar} from 'notistack'
import {shorterHash} from '../utils'
import {API, getEventStream, useWebSocket} from '../api'

type BlockchainContextType = {
    blockchainList?: any[]
    loadBlockchainList?: (from, to) => Promise<any>
}

export const BlockchainContext = createContext<Partial<BlockchainContextType>>({})

export const BlockchainContextProvider = (props) => {
    const {enqueueSnackbar, closeSnackbar} = useSnackbar()
    const [blockchainList, setBlockchainList] = useState([])

    const blockchainListApiCall = (from?: number, to?: number) => {
        return API.get(
            'blocks/',
            from &&
            to && {
                params: {
                    from: from,
                    to: to,
                },
            },
        )
    }

    const loadBlockchainList = (from?: number, to?: number) => {
        return blockchainListApiCall(from, to).then((response) => {
            setBlockchainList(_.concat(blockchainList, response.data))
            return new Promise((resolve) => {
                resolve()
            })
        })
    }

    const handleNewBlock = () => {
        if (!_.isEmpty(blockchainList)) {
            blockchainListApiCall(_.head(blockchainList).index, _.head(blockchainList).index + 1).then((response) => {
                setBlockchainList(_.concat(response.data, blockchainList))
                enqueueSnackbar('New block mined ' + shorterHash(_.head<any>(response.data)?.hash), {
                    variant: 'info',
                })
            })
        }
    }

    useEffect(() => {
        const sse = getEventStream('block')
        sse.onmessage = (message) => {
            console.info('INFO: New block mined')
            handleNewBlock()
        }
    }, [])

    return (
        <>
            <BlockchainContext.Provider value={{blockchainList, loadBlockchainList}}>
                {props.children}
            </BlockchainContext.Provider>
        </>
    )
}
