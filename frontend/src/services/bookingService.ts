import api from './api';
import { Booking, BookingStatus } from '../types/booking';

export const bookingService = {
    getAllBookings: () => api.get('/api/bookings'),
    getBookingById: (id: number) => api.get(`/api/bookings/${id}`),
    createBooking: (booking: Booking) => api.post('/api/bookings', booking),
    updateBooking: (id: number, booking: Booking) => api.put(`/api/bookings/${id}`, booking),
    deleteBooking: (id: number) => api.delete(`/api/bookings/${id}`),
    getBookingsByStatus: (status: BookingStatus) => api.get(`/api/bookings/status/${status}`),
    getBookingsByCustomer: (customerId: number) => api.get(`/api/bookings/customer/${customerId}`),
};