export enum BookingStatus {
    CONFIRMED = 'CONFIRMED',
    PENDING = 'PENDING',
    CANCELLED = 'CANCELLED',
    COMPLETED = 'COMPLETED'
}

export interface Booking {
    id?: number;
    customerId: number;
    roomId: number;
    checkInDate: string;
    checkOutDate: string;
    status: BookingStatus;
    totalAmount: number;
    numberOfGuests: number;
    specialRequests?: string;
    createdAt?: string;
    updatedAt?: string;
}