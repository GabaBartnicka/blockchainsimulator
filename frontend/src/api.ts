import axios from 'axios'
import {w3cwebsocket} from 'websocket'
import {useCallback, useMemo} from 'react'

export const API = axios.create({
    // baseURL: `http://localhost:8080/`,
    baseURL: `/`,
})

export const miningStatus = (callback) => {
    const interval = setInterval(() => {
        API.get('/v0/mine/status').then((response) => callback(response))
    }, 5000)
}

export const useWebSocket = (path) => {
    return useMemo(() => {
        return new w3cwebsocket(`ws://localhost:8080/e/${path}`)
    }, [path])
}

export const getEventStream = (path) => {
    console.log('SSE path', path)
    if (!!window.EventSource) {
        return new EventSource(`http://localhost:8080/events/${path}`)
    } else {
        // Result to xhr polling :(
    }
}
