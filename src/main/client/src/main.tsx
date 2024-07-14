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
import {toast, ToastContainer} from "react-toastify";
import axios from "axios";

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
axios.interceptors.response.use(response => response, error => {
    toast.error("Failed to fetch data", {
        position: "top-center",
        autoClose: false
    });
    return Promise.reject(error);
});
ReactDOM.createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <RouterProvider router={router}/>
        <ToastContainer/>
    </React.StrictMode>,
)