import { useState } from "react";
import { registerUser } from "../services/AuthService";
import toast from "react-hot-toast";
import { useNavigate } from "react-router";

const Register = () => {
  const [registerData, setRegisterData] = useState({
    name: "",
    email: "",
    password: "",
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    setRegisterData({
      ...registerData,
      [e.target.name]: e.target.value,
    });
  };

  const handleRegister = async (e) => {
    e.preventDefault();

    if (registerData.name.trim() === "" || registerData.email.trim() === "" || registerData.password.trim() === "") {
      toast.error("All fields are required");
      return;
    }

    try {
      const data = await registerUser(registerData);

      localStorage.setItem("token", data.token);

      toast.success("Registration successful");
      console.log("JWT Token:", data.token);
      navigate("/");
    } catch (error) {
      console.error(error);
      toast.error(error.response?.data?.message || "Registration failed");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center">
      <div className="p-8 flex flex-col gap-6 w-full max-w-md rounded bg-blue-200 shadow border border-blue-500">
        <h1 className="text-2xl font-medium text-center">Register</h1>

        <form onSubmit={handleRegister}>
          <label className="block font-medium mb-2">Name:</label>
          <input
            type="text"
            name="name"
            value={registerData.name}
            onChange={handleChange}
            placeholder="Enter name"
            className="w-full bg-gray-300 rounded-lg px-4 py-2 border-2 border-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-400 mb-5"
          />

          <label className="block font-medium mb-2">Email:</label>
          <input
            type="email"
            name="email"
            value={registerData.email}
            onChange={handleChange}
            placeholder="Enter email"
            className="w-full bg-gray-300 rounded-lg px-4 py-2 border-2 border-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-400 mb-5"
          />

          <label className="block font-medium mb-2">Password:</label>
          <input
            type="password"
            name="password"
            value={registerData.password}
            onChange={handleChange}
            placeholder="Enter password"
            className="w-full bg-gray-300 rounded-lg px-4 py-2 border-2 border-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-400 mb-5"
          />

          <button
            type="submit"
            className="w-full px-3 py-2 bg-blue-800 hover:bg-blue-600 rounded cursor-pointer text-blue-200"
          >
            Register
          </button>

          <p className="text-center mt-4">
            Already registered?{" "}
            <button
              type="button"
              onClick={() => navigate("/login")}
              className="text-blue-800 underline"
            >
              Login
            </button>
          </p>
        </form>
      </div>
    </div>
  );
};

export default Register;