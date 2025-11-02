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
    <BrowserRouter>
    <ChatProvider>
    <Toaster/>
    <Routes>
      <Route path="/" element={<App /> }/>
      <Route path="/chat" element={<ChatPage/>}/>
    </Routes>
    </ChatProvider>
    </BrowserRouter>
)
