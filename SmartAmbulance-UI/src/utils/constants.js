export const EMERGENCY_TYPES = [
    'MEDICAL_EMERGENCY',
    'MEDICAL',
    'FIRE',
    'ACCIDENT',
    'OTHER',
]

export const EMERGENCY_STATUSES = [
    'CREATED',
    'VALIDATED',
    'WAITING_FOR_ASSIGNMENT',
    'ASSIGNED',
    'IN_PROGRESS',
    'NO_AMBULANCE_AVAILABLE',
    'CANCELLED',
    'COMPLETED',
]

export const AMBULANCE_TYPES = ['BASIC', 'ADVANCED', 'ICU']

export const AMBULANCE_STATUSES = [
    'AVAILABLE',
    'ASSIGNED',
    'ON_TRIP',
    'OFFLINE',
    'MAINTENANCE',
]

export const ASSIGNMENT_STATUSES = [
    'ASSIGNED',
    'ACCEPTED',
    'REJECTED',
    'COMPLETED',
    'CANCELLED',
]

export const BED_TYPES = ['GENERAL', 'ICU', 'VENTILATOR']

export const BED_STATUSES = ['AVAILABLE', 'RESERVED', 'OCCUPIED', 'OUT_OF_SERVICE']

export const TRIP_STATUSES = [
    'ASSIGNED',
    'EN_ROUTE',
    'ARRIVED',
    'PATIENT_PICKED_UP',
    'HOSPITAL_REACHED',
    'COMPLETED',
    'CANCELLED',
]

export const NOTIFICATION_STATUSES = ['PENDING', 'SENT', 'FAILED']

// Map ka default center (India)
export const DEFAULT_CENTER = [20.2961, 85.8245]