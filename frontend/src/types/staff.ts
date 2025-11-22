export enum StaffRole {
    MANAGER = 'MANAGER',
    RECEPTIONIST = 'RECEPTIONIST',
    HOUSEKEEPING = 'HOUSEKEEPING',
    MAINTENANCE = 'MAINTENANCE'
}

export enum StaffStatus {
    ACTIVE = 'ACTIVE',
    INACTIVE = 'INACTIVE',
    ON_LEAVE = 'ON_LEAVE'
}

export interface Staff {
    id?: number;
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    role: StaffRole;
    status: StaffStatus;
    joinDate: string;
    salary: number;
    address?: string;
}