import { httpClient } from "../config/AxiosHelper"

export const registerUser=async(loginData)=>{
    const response=await httpClient.post("/api/v1/auth/register",loginData);
    return response.data;
}

export const loginUser=async(registerData)=>{
    const response=await httpClient.post("/api/v1/auth/login",registerData);
    return response.data;
}