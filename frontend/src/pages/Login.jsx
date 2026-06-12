import React, { useState } from "react";
import { useNavigate } from "react-router";
import toast from "react-hot-toast";
import { loginUser } from "../services/AuthService";
import useChatContext from "../context/ChatContext";

const Login = () => {
  const navigate = useNavigate();
  const { setCurrentUser } = useChatContext();

  const [loginData, setLoginData] = useState({
    email: "",
    password: "",
  });

  function handleChange(event) {
    setLoginData({
      ...loginData,
      [event.target.name]: event.target.value,
    });
  }

  async function handleLogin(event) {
    event.preventDefault();

    if (loginData.email.trim() === "" || loginData.password.trim() === "") {
      toast.error("Email and password are required");
      return;
    }

    try {
      const response = await loginUser(loginData);

      localStorage.setItem("token", response.token);

      toast.success("Login successful");
      setCurrentUser(response.name || loginData.email);
      navigate("/home");
    } catch (error) {
      console.log(error);
      toast.error("Invalid email or password");
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center">
      <div className="p-8 flex flex-col gap-6 w-full max-w-md rounded bg-blue-200 shadow border border-blue-500">
        <h1 className="text-2xl font-medium text-center">Login</h1>

        <form onSubmit={handleLogin}>
          <label className="block font-medium mb-2">Email:</label>
          <input
            type="email"
            name="email"
            value={loginData.email}
            onChange={handleChange}
            placeholder="Enter email"
            className="w-full bg-gray-300 rounded-lg px-4 py-2 border-2 border-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-400 mb-5"
          />

          <label className="block font-medium mb-2">Password:</label>
          <input
            type="password"
            name="password"
            value={loginData.password}
            onChange={handleChange}
            placeholder="Enter password"
            className="w-full bg-gray-300 rounded-lg px-4 py-2 border-2 border-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-400 mb-5"
          />

          <button
            type="submit"
            className="w-full px-3 py-2 bg-blue-800 hover:bg-blue-600 rounded cursor-pointer text-blue-200"
          >
            Login
          </button>

          <p className="text-center mt-4">
            New user?{" "}
            <button
              type="button"
              onClick={() => navigate("/register")}
              className="text-blue-800 underline"
            >
              Register
            </button>
          </p>
        </form>
      </div>
    </div>
  );
};

export default Login;