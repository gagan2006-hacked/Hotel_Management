import { AxiosError } from 'axios';

interface ApiError {
    message: string;
    code?: string;
    details?: any;
}

export const handleApiError = (error: AxiosError): ApiError => {
    if (error.response) {
        // Server responded with a status code outside of 2xx
        const status = error.response.status;
        const data = error.response.data as any;

        switch (status) {
            case 400:
                return {
                    message: data.message || 'Invalid request',
                    code: 'BAD_REQUEST',
                    details: data
                };
            case 401:
                return {
                    message: 'Please login to continue',
                    code: 'UNAUTHORIZED'
                };
            case 403:
                return {
                    message: 'You do not have permission to perform this action',
                    code: 'FORBIDDEN'
                };
            case 404:
                return {
                    message: 'The requested resource was not found',
                    code: 'NOT_FOUND'
                };
            case 500:
                return {
                    message: 'An internal server error occurred',
                    code: 'SERVER_ERROR'
                };
            default:
                return {
                    message: data.message || 'An unexpected error occurred',
                    code: 'UNKNOWN_ERROR',
                    details: data
                };
        }
    } else if (error.request) {
        // Request was made but no response received
        return {
            message: 'Unable to connect to the server',
            code: 'NETWORK_ERROR'
        };
    } else {
        // Error in setting up the request
        return {
            message: error.message || 'An error occurred while setting up the request',
            code: 'REQUEST_SETUP_ERROR'
        };
    }
};