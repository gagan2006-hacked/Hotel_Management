import api from './api';
import { Staff, StaffRole } from '../types/staff';

export const staffService = {
    getAllStaff: () => api.get('/api/staff'),
    getStaffById: (id: number) => api.get(`/api/staff/${id}`),
    createStaff: (staff: Staff) => api.post('/api/staff', staff),
    updateStaff: (id: number, staff: Staff) => api.put(`/api/staff/${id}`, staff),
    deleteStaff: (id: number) => api.delete(`/api/staff/${id}`),
    getStaffByRole: (role: StaffRole) => api.get(`/api/staff/role/${role}`),
};