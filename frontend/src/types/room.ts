export enum RoomType {
    SINGLE = 'SINGLE',
    DOUBLE = 'DOUBLE',
    SUITE = 'SUITE',
    DELUXE = 'DELUXE'
}

export enum RoomStatus {
    AVAILABLE = 'AVAILABLE',
    OCCUPIED = 'OCCUPIED',
    MAINTENANCE = 'MAINTENANCE',
    RESERVED = 'RESERVED'
}

export interface Room {
    id?: number;
    roomNumber: string;
    type: RoomType;
    status: RoomStatus;
    pricePerNight: number;
    floor: number;
    description?: string;
}