import { prettyEnum } from '../utils/format'

const COLORS = {
    // success
    COMPLETED: 'success',
    AVAILABLE: 'success',
    SENT: 'success',
    ACCEPTED: 'success',
    HOSPITAL_REACHED: 'success',
    // danger
    CANCELLED: 'danger',
    REJECTED: 'danger',
    FAILED: 'danger',
    NO_AMBULANCE_AVAILABLE: 'danger',
    OUT_OF_SERVICE: 'danger',
    // warning
    CREATED: 'warning',
    VALIDATED: 'warning',
    WAITING_FOR_ASSIGNMENT: 'warning',
    PENDING: 'warning',
    RESERVED: 'warning',
    MAINTENANCE: 'warning',
    OFFLINE: 'warning',
    // info
    ASSIGNED: 'info',
    IN_PROGRESS: 'info',
    EN_ROUTE: 'info',
    ON_TRIP: 'info',
    OCCUPIED: 'info',
    ARRIVED: 'primary',
    PATIENT_PICKED_UP: 'primary',
}

export default function StatusBadge({ value }) {
    if (!value) return <span className="badge">-</span>
    const color = COLORS[value] || ''
    return <span className={`badge ${color ? `badge-${color}` : ''}`}>{prettyEnum(value)}</span>
}