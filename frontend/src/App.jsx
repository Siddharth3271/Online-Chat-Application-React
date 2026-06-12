import './App.css'
import Home from './components/Home'
import Register from "./pages/Register";
import Login from "./pages/Login";
import { Routes, Route, Navigate } from "react-router";
import ChatPage from './pages/ChatPage';

function App() {

  return (
    <Routes>
      <Route path="/" element={<Navigate to="/login" />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/home" element={<Home />} />
      <Route path="/chat" element={<ChatPage />} />
    </Routes>
  )
}

export default App
