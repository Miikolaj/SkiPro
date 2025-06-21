import axios from 'axios';

const apiClient = axios.create({
	baseURL: '/api',
});

apiClient.interceptors.response.use(response => {
	return response;
}, async error => {
	console.error('Error:', error);
	return Promise.reject(error);
});

export default apiClient;