import {Divider, Drawer, List, ListItem, ListItemIcon, ListItemText} from "@material-ui/core";
import {Dashboard as DashboardIcon, Money as MoneyIcon, ScatterPlot as ScatterPlotIcon} from "@material-ui/icons";
import React from "react";
import {Link, NavLink} from "react-router-dom";
import {CustomLink} from "../common/CustomLink";

export const Menu = () => {
    return (
        <>
            <List>
                <ListItem button component={CustomLink} to='/'>
                    <ListItemIcon>
                        <DashboardIcon/>
                    </ListItemIcon>
                    <ListItemText primary="Dashboard"/>
                </ListItem>
                <Divider/>
                <ListItem button component={CustomLink} to='/blockchain'>
                    <ListItemIcon>
                        <MoneyIcon/>
                    </ListItemIcon>
                    <ListItemText primary="Blockchain"/>
                </ListItem>
                <ListItem button component={CustomLink} to='/transactions'>
                    <ListItemIcon>
                        <MoneyIcon/>
                    </ListItemIcon>
                    <ListItemText primary="Transactions"/>
                </ListItem>
                <ListItem button component={CustomLink} to='/nodes'>
                    <ListItemIcon>
                        <ScatterPlotIcon/>
                    </ListItemIcon>
                    <ListItemText primary="Nodes"/>
                </ListItem>
            </List>
        </>
    )
}