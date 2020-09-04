import React, {FC, useState} from 'react'
import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    Fab,
    TextField,
} from '@material-ui/core'
import {makeStyles} from '@material-ui/core/styles'
import {Add as AddIcon} from '@material-ui/icons'
import {API} from '../api'
import {useSnackbar} from 'notistack'

const useStyles = makeStyles((theme) => ({
    fab: {
        position: 'absolute',
        bottom: theme.spacing(2),
        right: theme.spacing(2),
        zIndex: 1000,
    },
}))

type newNodeFormType = { name: string; host: string; port: number }
const initialFormData = {name: null, host: null, port: null}

export const NewNodeDialog: FC<any> = () => {
    const classes = useStyles()
    const [open, setOpen] = useState<boolean>(false)
    const [formData, setFormData] = useState<newNodeFormType>(initialFormData)
    const snackbar = useSnackbar()

    const handleOpen = () => {
        setOpen(true)
    }

    const handleClose = () => {
        setFormData(initialFormData)
        setOpen(false)
    }

    const handleAdd = () => {
        const {name, port, host} = formData
        // const {name, label, amount} = formData
        if (name && port && host) {
            API.put('/v0/peer/new', {name, host, port})
                .then((response) => {
                    setFormData(initialFormData)
                    handleClose()
                })
                .catch((error) => {
                    snackbar.enqueueSnackbar(error.toString(), {
                        variant: 'error',
                    })
                })
        }
    }

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({...formData, [event.target.name]: event.target.value})
    }

    return (
        <>
            <Fab color="primary" aria-label="add" className={classes.fab} onClick={handleOpen}>
                <AddIcon/>
            </Fab>
            <Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
                <DialogTitle id="form-dialog-title">Add new peer</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        To subscribe to this website, please enter your email address here. We will send updates
                        occasionally.
                    </DialogContentText>
                    <TextField
                        autoFocus
                        required
                        margin="dense"
                        name="name"
                        label="Peer name"
                        type="text"
                        fullWidth
                        onChange={handleChange}
                    />
                    <TextField
                        required
                        margin="dense"
                        name="host"
                        label="IP address"
                        type="text"
                        fullWidth
                        onChange={handleChange}
                    />
                    <TextField
                        required
                        margin="dense"
                        name="port"
                        label="Port"
                        type="number"
                        fullWidth
                        onChange={handleChange}

                        defaultValue={0.0}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose} color="primary">
                        Cancel
                    </Button>
                    <Button onClick={handleAdd} color="primary">
                        Add
                    </Button>
                </DialogActions>
            </Dialog>
        </>
    )
}
