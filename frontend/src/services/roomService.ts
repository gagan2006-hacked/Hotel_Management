import api from './api';
import { Room, RoomType, RoomStatus } from '../types/room';

export const roomService = {
    getAllRooms: () => api.get('/api/rooms'),
    getRoomById: (id: number) => api.get(`/api/rooms/${id}`),
    createRoom: (room: Room) => api.post('/api/rooms', room),
    updateRoom: (id: number, room: Room) => api.put(`/api/rooms/${id}`, room),
    deleteRoom: (id: number) => api.delete(`/api/rooms/${id}`),
    getRoomsByType: (type: RoomType) => api.get(`/api/rooms/type/${type}`),
    getRoomsByStatus: (status: RoomStatus) => api.get(`/api/rooms/status/${status}`),
};