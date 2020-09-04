import {makeStyles} from "@material-ui/core/styles";
import React from "react";
import {Container} from "@material-ui/core";
import {Route, Switch} from "react-router-dom";
import {DashboardView} from "../dashboard/DashboardView";
import {BlockchainPage} from "../blockchain/BlockchainPage";
import {NodesPage} from "../nodes/NodesPage";
import {Bottom} from "./Bottom";
import {TransactionPage} from "../transactions/TransactionsPage";

const useStyles = makeStyles((theme) => ({
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
}));

export const Main = () => {

    const classes = useStyles();

    return (
        <main className={classes.content}>
            <div className={classes.appBarSpacer}/>
            <Container className={classes.container} maxWidth={"xl"}>
                <Switch>
                    <Route path="/blockchain">
                        <BlockchainPage/>
                    </Route>
                    <Route path="/nodes">
                        <NodesPage/>
                    </Route>
                    <Route path="/transactions">
                        <TransactionPage/>
                    </Route>
                    <Route>
                        <DashboardView/>
                    </Route>
                </Switch>
            </Container>
            <Bottom/>
        </main>
    )

}