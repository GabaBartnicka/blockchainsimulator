import {Box, Container} from "@material-ui/core";
import React from "react";
import {makeStyles} from "@material-ui/core/styles";
import {Copyright} from "@material-ui/icons";

const useStyles = makeStyles((theme) => ({
    fixedDown: {
        position: 'fixed',

    }
}));
export const Bottom = () => {


    return (
        <Box p={2} position={'relative'} bottom={0} textAlign={'center'} width={1}>
            Gabriela Bartnicka - UJ - 2020
        </Box>
    )
}