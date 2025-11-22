import api from '../services/api';

interface LoginCredentials {
    username: string;
    password: string;
}

interface AuthResponse {
    token: string;
    user: {
        id: number;
        username: string;
        role: string;
    };
}

export const authService = {
    login: async (credentials: LoginCredentials) => {
        try {
            const response = await api.post<AuthResponse>('/api/auth/login', credentials);
            if (response.data.token) {
                localStorage.setItem('authToken', response.data.token);
                localStorage.setItem('user', JSON.stringify(response.data.user));
            }
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    logout: () => {
        localStorage.removeItem('authToken');
        localStorage.removeItem('user');
        window.location.href = '/login';
    },

    getCurrentUser: () => {
        const user = localStorage.getItem('user');
        return user ? JSON.parse(user) : null;
    },

    isAuthenticated: () => {
        return !!localStorage.getItem('authToken');
    },

    getToken: () => {
        return localStorage.getItem('authToken');
    }
};