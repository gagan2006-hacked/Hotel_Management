export enum PaymentStatus {
    PENDING = 'PENDING',
    COMPLETED = 'COMPLETED',
    FAILED = 'FAILED',
    REFUNDED = 'REFUNDED'
}

export enum PaymentMode {
    CREDIT_CARD = 'CREDIT_CARD',
    DEBIT_CARD = 'DEBIT_CARD',
    CASH = 'CASH',
    BANK_TRANSFER = 'BANK_TRANSFER'
}

export interface Payment {
    id?: number;
    bookingId: number;
    amount: number;
    status: PaymentStatus;
    mode: PaymentMode;
    transactionId?: string;
    paymentDate: string;
    notes?: string;
}