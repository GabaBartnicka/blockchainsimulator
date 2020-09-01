import React from 'react';
import logo from './logo.svg';
import './App.css';
import {createMuiTheme, CssBaseline, MuiThemeProvider} from "@material-ui/core";
import {Top} from "./layout/Top";
import {makeStyles} from "@material-ui/core/styles";
import {Main} from "./layout/Main";
import {
    BrowserRouter,
    Switch,
    Route,
    Link
} from "react-router-dom";
import {SnackbarProvider} from "notistack";
import {Bottom} from "./layout/Bottom";

const drawerWidth = 240;

const theme = createMuiTheme({
    palette: {
        type: "dark"
    }
});

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
}));

function App() {
    const classes = useStyles();
    return (
        <MuiThemeProvider theme={theme}>
            <SnackbarProvider maxSnack={3}>
                <BrowserRouter>
                    <div className={classes.root}>
                        <CssBaseline/>
                        <Top/>
                        <Main/>
                    </div>
                </BrowserRouter>
            </SnackbarProvider>
        </MuiThemeProvider>
    );

}

export default App;
