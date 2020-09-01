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
import InputAdornment from '@material-ui/core/InputAdornment'

const useStyles = makeStyles((theme) => ({
    fab: {
        position: 'absolute',
        bottom: theme.spacing(2),
        right: theme.spacing(2),
        zIndex: 1000,
    },
}))

type newTransactionFormType = { name: string; label: string; amount: number }

const initialFormData = {name: null, label: null, amount: null}

export const NewTransactionDialog: FC<any> = () => {
    const classes = useStyles()
    const [open, setOpen] = useState<boolean>(false)
    const [formData, setFormData] = useState<newTransactionFormType>(initialFormData)
    const snackbar = useSnackbar()

    const handleOpen = () => {
        setOpen(true)
    }

    const handleClose = () => {
        setFormData(initialFormData)
        setOpen(false)
    }

    const handleAdd = () => {
        const {name, label, amount} = formData
        if (name && label && amount) {
            API.post('/transaction', {publicAddress: {name, label}, amount})
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

    const validateAmount = formData.amount < 0

    return (
        <>
            <Fab color="primary" aria-label="add" className={classes.fab} onClick={handleOpen}>
                <AddIcon/>
            </Fab>
            <Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
                <DialogTitle id="form-dialog-title">Add new transaction</DialogTitle>
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
                        label="Public Address Name"
                        type="text"
                        fullWidth
                        onChange={handleChange}
                    />
                    <TextField
                        required
                        margin="dense"
                        name="label"
                        label="Public Address Label"
                        type="text"
                        fullWidth
                        onChange={handleChange}
                    />
                    <TextField
                        required
                        margin="dense"
                        name="amount"
                        label="Amount"
                        type="number"
                        fullWidth
                        value={formData.amount}
                        onChange={handleChange}
                        error={validateAmount}
                        InputProps={{
                            startAdornment: <InputAdornment position="start">₲</InputAdornment>,
                        }}
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
