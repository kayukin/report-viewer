import './ListReports.css'
import {AppBar, Backdrop, Box, CircularProgress, IconButton, Toolbar, Typography} from "@mui/material";
import MenuIcon from '@mui/icons-material/Home';
import {useEffect, useState} from "react";
import {Env} from "../../Env.ts";
import {DataGrid, GridColDef} from '@mui/x-data-grid';
import {Link} from "react-router-dom";
import {encodeQuery} from "../../Utils.ts";
import {Report} from "../../dto/Report.ts";

export const ListReports = () => {
    const columns: GridColDef[] = [
        {field: 'id', headerName: 'Id', width: 150},
        {
            field: 'name',
            headerName: 'Name',
            width: 300,
            renderCell: params =>
                <Link to={'/view?' + encodeQuery({key: params.row.name})}>{params.row.name}</Link>
        },
    ];

    const [reports, setReports] = useState<object[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch(`${Env.API_BASE_URL}/reports/`)
            .then(response => response.json() as Promise<Report[]>)
            .then(body => {
                setReports(body);
                setLoading(false);
            });
    }, []);

    return (
        <>
            <Backdrop
                sx={{color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1}}
                open={loading}
            >
                <CircularProgress color="inherit"/>
            </Backdrop>
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
                <Box height={'calc(100vh - 64px)'}
                     sx={{overflow: 'hidden'}}>
                    <DataGrid sx={{marginX: '2%'}} columns={columns} rows={reports}/>
                </Box>
            </Box>
        </>
    )
}