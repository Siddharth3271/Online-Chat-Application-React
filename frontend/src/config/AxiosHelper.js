import axios from "axios";
export const baseURL = "https://online-chat-application-react.onrender.com";
export const httpClient = axios.create({
  baseURL: baseURL,
});