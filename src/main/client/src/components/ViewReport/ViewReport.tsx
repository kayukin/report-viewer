import './ViewReport.css'
import {Link, useSearchParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {Env} from "../../Env.ts"
import {AppBar, Backdrop, Box, Button, CircularProgress, IconButton, Toolbar, Typography} from "@mui/material";
import MenuIcon from "@mui/icons-material/Home";
import {encodeQuery} from "../../Utils.ts";
import {IFrame} from "../IFrame"
import axios from "axios";

export const ViewReport = () => {
    const [searchParams] = useSearchParams();
    const key = searchParams.get("key")!;
    const [loading, setLoading] = useState(true);
    const [reports, setReports] = useState<string[]>([]);
    const [index, setIndex] = useState(0);

    useEffect(() => {
        axios.get(`${Env.API_BASE_URL}/reports/view?` + encodeQuery({key}))
            .then(value => {
                setReports(value.data);
            })
            .catch(() => setLoading(false));
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
                    <Button variant="text"
                            color="inherit"
                            onClick={() => {
                                setIndex(index - 1)
                                setLoading(true)
                            }}
                            disabled={index <= 0}
                    >
                        Previous
                    </Button>
                    <Button variant="text"
                            color="inherit"
                            onClick={() => {
                                setIndex(index + 1)
                                setLoading(true)
                            }}
                            disabled={index >= reports.length - 1}
                    >
                        Next
                    </Button>
                </Toolbar>
            </AppBar>
            <Toolbar/>
            <Box height={'calc(100vh - 64px)'}
                 sx={{overflow: 'hidden'}}>
                <IFrame src={reports[index]} onLoad={() => setLoading(reports.length == 0)}/>
            </Box>
        </>
    );
};