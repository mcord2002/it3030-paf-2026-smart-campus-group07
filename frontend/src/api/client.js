import axios from 'axios';

const baseURL = process.env.REACT_APP_API_BASE || 'http://localhost:8080/api/v1';

export const api = axios.create({ baseURL });

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('campus_hub_token');
  if (token) {
    // eslint-disable-next-line no-param-reassign
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (r) => r,
  (err) => {
    const msg =
      err.response?.data?.message ||
      err.response?.data?.error ||
      err.message ||
      'Request failed';
    const enriched = new Error(msg);
    enriched.status = err.response?.status;
    enriched.details = err.response?.data;
    return Promise.reject(enriched);
  }
);
