import './ViewReport.css'
import {Link, useSearchParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {Env} from "../../Env.ts"
import {AppBar, Backdrop, Box, CircularProgress, IconButton, Toolbar, Typography} from "@mui/material";
import MenuIcon from "@mui/icons-material/Home";
import {encodeQuery} from "../../Utils.ts";
import {IFrame} from "../IFrame"
import axios from "axios";

export const ViewReport = () => {
    const [searchParams] = useSearchParams();
    const key = searchParams.get("key")!;
    const [link, setLink] = useState('');
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        axios.get(`${Env.API_BASE_URL}/reports/view?` + encodeQuery({key}))
            .then(value => {
                setLink(value.data);
            })
            .finally(() => setLoading(false));
    }, [key]);

    return (
        <>
            <Backdrop
                sx={{color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1}}
                open={loading}
            >
                <CircularProgress color="inherit"/>
            </Backdrop>
            <AppBar position="fixed">
                <Toolbar>
                    <IconButton
                        component={Link}
                        to="/"
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
            <Box height={'calc(100vh - 64px)'}
                 sx={{overflow: 'hidden'}}>
                <IFrame src={link} onLoad={() => setLoading(link == '')}/>
            </Box>
        </>
    );
};