import axios from "axios";

const API_URL = "http://localhost:8080/api";

const api = axios.create({
  baseURL: API_URL,
});

api.interceptors.request.use(
  (config) => {
    const userId = localStorage.getItem("userId");
    const token = localStorage.getItem("token");

    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }

    if (userId) {
      config.headers["X-User-ID"] = userId;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export const getActivities = () =>
  api.get("/activities").catch((error) => {
    console.error(error);
    return [];
  });

export const addActivity = (activity) =>
  api.post("/activities", activity).catch((error) => {
    console.error(error);
    return [];
  });

export const getActivityDetail = (id) =>
  api.get(`/recommendations/activity/${id}`).catch((error) => {
    console.error(error);
    return [];
  });
