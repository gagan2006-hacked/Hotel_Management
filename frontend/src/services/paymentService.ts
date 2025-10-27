import api from './api';
import { Payment, PaymentStatus, PaymentMode } from '../types/payment';

export const paymentService = {
    getAllPayments: () => api.get('/api/payments'),
    getPaymentById: (id: number) => api.get(`/api/payments/${id}`),
    createPayment: (payment: Payment) => api.post('/api/payments', payment),
    updatePayment: (id: number, payment: Payment) => api.put(`/api/payments/${id}`, payment),
    getPaymentsByBooking: (bookingId: number) => api.get(`/api/payments/booking/${bookingId}`),
    getPaymentsByStatus: (status: PaymentStatus) => api.get(`/api/payments/status/${status}`),
};