import {
    AppBar,
    Badge,
    Box,
    CircularProgress,
    Divider,
    Drawer,
    IconButton,
    LinearProgress,
    List,
    Toolbar,
    Typography,
} from '@material-ui/core'
import React, {useEffect, useState} from 'react'
import {
    Menu as MenuIcon,
    Notifications as NotificationsIcon,
    ChevronLeft as ChevronLeftIcon,
} from '@material-ui/icons'
import clsx from 'clsx'
import {makeStyles} from '@material-ui/core/styles'
import {Menu} from './Menu'
import {getEventStream, miningStatus} from '../api'
import {useSnackbar} from 'notistack'

const drawerWidth = 240

const useStyles = makeStyles((theme) => ({
    root: {
        display: 'flex',
    },
    toolbar: {
        paddingRight: 24, // keep right padding when drawer closed
    },
    toolbarIcon: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'flex-end',
        padding: '0 8px',
        ...theme.mixins.toolbar,
    },
    appBar: {
        zIndex: theme.zIndex.drawer + 1,
        transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
        }),
    },
    appBarShift: {
        marginLeft: drawerWidth,
        width: `calc(100% - ${drawerWidth}px)`,
        transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    },
    menuButton: {
        marginRight: 36,
    },
    menuButtonHidden: {
        display: 'none',
    },
    title: {
        flexGrow: 1,
    },
    drawerPaper: {
        position: 'relative',
        whiteSpace: 'nowrap',
        width: drawerWidth,
        transition: theme.transitions.create('width', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    },
    drawerPaperClose: {
        overflowX: 'hidden',
        transition: theme.transitions.create('width', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
        }),
        width: theme.spacing(7),
        [theme.breakpoints.up('sm')]: {
            width: theme.spacing(9),
        },
    },
    appBarSpacer: theme.mixins.toolbar,
    content: {
        flexGrow: 1,
        height: '100vh',
        overflow: 'auto',
    },
    container: {
        paddingTop: theme.spacing(4),
        paddingBottom: theme.spacing(4),
    },
    paper: {
        padding: theme.spacing(2),
        display: 'flex',
        overflow: 'auto',
        flexDirection: 'column',
    },
    fixedHeight: {
        height: 240,
    },
}))

export const Top = () => {
    const classes = useStyles()
    const [open, setOpen] = useState(false)
    const [mining, setMining] = useState<any>(false)
    const snackbar = useSnackbar()

    const handleOpen = () => setOpen(true)
    const handleClose = () => setOpen(false)

    useEffect(() => {
        const miningStream = getEventStream('mining')
        miningStream.onmessage = (message) => {
            const miningStatus = JSON.parse(message?.data)?.busy
            console.log('mining message', message)
            console.log('mining message status', mining, miningStatus)
            if (miningStatus) {
                snackbar.enqueueSnackbar('Mining started', {
                    variant: 'success',
                    preventDuplicate: true,
                })
            } else {
                snackbar.enqueueSnackbar('Mining ended', {
                    variant: 'success',
                    preventDuplicate: true,
                })
            }
            setMining(miningStatus)
        }
    }, [])

    return (
        <>
            <AppBar position="absolute" className={clsx(classes.appBar, open && classes.appBarShift)}>
                <Toolbar>
                    <IconButton
                        edge="start"
                        color="inherit"
                        aria-label="open drawer"
                        onClick={handleOpen}
                        className={clsx(classes.menuButton, open && classes.menuButtonHidden)}
                    >
                        <MenuIcon/>
                    </IconButton>
                    <Typography component="h1" variant="h6" color="inherit" noWrap className={classes.title}>
                        Blockchain UI
                    </Typography>
                    {mining && (
                        <Box p={1}>
                            <Typography variant="h6" color="inherit" noWrap>
                                Mining in progress
                            </Typography>
                            <LinearProgress/>
                        </Box>
                    )}
                    {/*<IconButton color="inherit">*/}
                    {/*    <Badge badgeContent={4} color="secondary">*/}
                    {/*        <NotificationsIcon/>*/}
                    {/*    </Badge>*/}
                    {/*</IconButton>*/}
                </Toolbar>
            </AppBar>
            <Drawer
                variant="permanent"
                classes={{
                    paper: clsx(classes.drawerPaper, !open && classes.drawerPaperClose),
                }}
                open={open}
            >
                <div className={classes.toolbarIcon}>
                    <IconButton onClick={handleClose}>
                        <ChevronLeftIcon/>
                    </IconButton>
                </div>
                <Divider/>
                <Menu/>
            </Drawer>
        </>
    )
}
