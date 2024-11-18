import axios from 'axios'
import ElementUI from 'element-ui'

// Create an axios instance with default configuration
const request = axios.create({
    baseURL: 'http://localhost:9090', // Base URL for API requests
    timeout: 5000 // Request timeout duration in milliseconds
})

// Request interceptor
// Allows handling requests before they are sent, such as adding tokens or encrypting parameters
request.interceptors.request.use(
    config => {
        // Set default headers for all requests
        config.headers['Content-Type'] = 'application/json;charset=utf-8';

        return config; // Continue with the request
    },
    error => {
        // Handle request errors
        return Promise.reject(error);
    }
);

// Response interceptor
// Allows unified processing of response data
request.interceptors.response.use(
    response => {
        let res = response.data; // Extract the response data

        // Handle file responses
        if (response.config.responseType === 'blob') {
            return res;
        }

        // Handle unauthorized access (401) by showing an error message
        if (res.code === '401') {
            ElementUI.Message({
                message: res.msg,
                type: 'error'
            });
        }

        // Ensure compatibility with server responses that return strings
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res;
        }

        return res; // Return the processed response
    },
    error => {
        console.log('err' + error); // Log errors for debugging
        return Promise.reject(error); // Reject the error for further handling
    }
);

export default request
