window.global = window;
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import {BrowserRouter, Route, Routes} from 'react-router'
import { Toaster } from 'react-hot-toast'
import ChatPage from './pages/ChatPage.jsx'
import { ChatProvider } from './context/ChatContext.jsx'

createRoot(document.getElementById('root')).render(
    <ChatProvider>
    <BrowserRouter>
    <App/>
    <Toaster/>
    </BrowserRouter>
    </ChatProvider>
)
