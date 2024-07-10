import './App.css'
import {AppBar, Box, IconButton, List, ListItem, ListItemButton, Toolbar, Typography} from "@mui/material";
import MenuIcon from '@mui/icons-material/Home';
import {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {Env} from "./Env.ts";
import {encodeQuery} from "./Utils.ts";

function App() {
    const [reports, setReports] = useState([])

    useEffect(() => {
        fetch(`${Env.API_BASE_URL}/reports/`)
            .then(response => response.json())
            .then(body => setReports(body));
    }, []);

    return (
        <>
            <Box sx={{width: '100%', bgcolor: 'background.paper'}}>
                <AppBar position="fixed">
                    <Toolbar>
                        <IconButton
                            size="large"
                            edge="start"
                            color="inherit"
                            aria-label="menu"
                            sx={{mr: 2}}
                        >
                            <MenuIcon/>
                        </IconButton>
                        <Typography variant="h6" component="div" sx={{flexGrow: 1}}>
                            Reports
                        </Typography>
                    </Toolbar>
                </AppBar>
                <Toolbar/>
                <List>
                    {reports.map(value =>
                        <ListItem key={value} component={Link} to={'/view?' + encodeQuery({key: value})}>
                            <ListItemButton>{value}</ListItemButton>
                        </ListItem>)}
                </List>
            </Box>
        </>
    )
}

export default App
