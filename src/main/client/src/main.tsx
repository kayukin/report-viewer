import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import '@fontsource/roboto/300.css';
import '@fontsource/roboto/400.css';
import '@fontsource/roboto/500.css';
import '@fontsource/roboto/700.css';
import {ViewReport} from './components/ViewReport'
import {ListReports} from "./components/ListReports";

const router = createBrowserRouter([
    {
        path: '/',
        element: <ListReports/>,
    },
    {
        path: '/view',
        element: <ViewReport/>,
    },
]);
ReactDOM.createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <RouterProvider router={router}/>
    </React.StrictMode>,
)