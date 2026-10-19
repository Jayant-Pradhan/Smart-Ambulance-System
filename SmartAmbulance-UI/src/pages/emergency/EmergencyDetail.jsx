import { useCallback, useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { getEmergencyById, cancelEmergency } from '../../api/emergencyApi'
import { getAssignmentByEmergencyId } from '../../api/assignmentApi'
import { getTrackingByEmergency } from '../../api/trackingApi'
import { getNotificationsByEmergency } from '../../api/notificationApi'
import { getErrorMessage } from '../../utils/errors'
import { formatDateTime, formatCoord, prettyEnum, yesNo } from '../../utils/format'
import { useToast } from '../../components/Toast'
import Loader from '../../components/Loader'
import EmptyState from '../../components/EmptyState'
import StatusBadge from '../../components/StatusBadge'
import MapView from '../../components/MapView'

function Item({ label, children }) {
    return (
        <div className="detail-item">
            <div className="k">{label}</div>
            <div className="v">{children}</div>
        </div>
    )
}

export default function EmergencyDetail() {
    const { id } = useParams()
    const toast = useToast()
    const [emergency, setEmergency] = useState(null)
    const [assignment, setAssignment] = useState(null)
    const [tracking, setTracking] = useState(null)
    const [notifications, setNotifications] = useState([])
    const [loading, setLoading] = useState(true)
    const [cancelling, setCancelling] = useState(false)

    const load = useCallback(async () => {
        try {
            const e = await getEmergencyById(id)
            setEmergency(e)
        } catch (err) {
            toast.error(getErrorMessage(err))
            setLoading(false)
            return
        }

        // Baaki data optional hai: assignment/tracking abhi na bane ho to 404 normal hai
        const [a, t, n] = await Promise.allSettled([
            getAssignmentByEmergencyId(id),
            getTrackingByEmergency(id),
            getNotificationsByEmergency(id),
        ])
        setAssignment(a.status === 'fulfilled' ? a.value : null)
        setTracking(t.status === 'fulfilled' ? t.value : null)
        setNotifications(n.status === 'fulfilled' ? n.value : [])
        setLoading(false)
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [id])

    useEffect(() => {
        load()
        const timer = setInterval(load, 5000)
        return () => clearInterval(timer)
    }, [load])

    async function handleCancel() {
        if (!window.confirm('Do you want to cancel this emergency?')) return
        setCancelling(true)
        try {
            const updated = await cancelEmergency(id)
            setEmergency(updated)
            toast.success('Emergency cancelled')
        } catch (err) {
            toast.error(getErrorMessage(err))
        } finally {
            setCancelling(false)
        }
    }

    if (loading) return <Loader />
    if (!emergency) {
        return (
            <div>
                <EmptyState message="No emergency found."/>
                <Link to="/emergency">Go back</Link>
            </div>
        )
    }

    const e = emergency
    const canCancel = e.status !== 'CANCELLED' && e.status !== 'COMPLETED'

    const markers = [{ lat: e.latitude, lng: e.longitude, label: `Emergency #${e.id}` }]
    if (tracking && tracking.latitude != null) {
        markers.push({
            lat: tracking.latitude,
            lng: tracking.longitude,
            label: `Ambulance #${tracking.ambulanceId}`,
        })
    }

    return (
        <div>
            <div className="page-header">
                <div>
                    <h1>
                        Emergency #{e.id} <StatusBadge value={e.status} />
                    </h1>
                    <p>Created: {formatDateTime(e.createdAt)}</p>
                </div>
                <div className="btn-row">
                    <Link to="/emergency" className="btn btn-secondary">
                        Back
                    </Link>
                    {tracking && (
                        <Link to={`/tracking/${e.id}`} className="btn btn-secondary">
                            Live tracking
                        </Link>
                    )}
                    {canCancel && (
                        <button className="btn btn-danger" onClick={handleCancel} disabled={cancelling}>
                            {cancelling ? 'Cancelling...' : 'Cancel Emergency'}
                        </button>
                    )}
                </div>
            </div>

            <div className="card">
                <h2>Details</h2>
                <div className="detail-grid">
                    <Item label="Caller">{e.callerName}</Item>
                    <Item label="Mobile">{e.callerMobile}</Item>
                    <Item label="Email">{e.callerEmail || '-'}</Item>
                    <Item label="Type">{prettyEnum(e.emergencyType)}</Item>
                    <Item label="Patients">{e.patientCount}</Item>
                    <Item label="Location">
                        {formatCoord(e.latitude)}, {formatCoord(e.longitude)}
                    </Item>
                    <Item label="Unconscious">{yesNo(e.unconscious)}</Item>
                    <Item label="Breathing difficulty">{yesNo(e.breathingDifficulty)}</Item>
                    <Item label="Severe bleeding">{yesNo(e.severeBleeding)}</Item>
                    <Item label="Trapped in vehicle">{yesNo(e.trappedInVehicle)}</Item>
                    <Item label="Last updated">{formatDateTime(e.updatedAt)}</Item>
                    <Item label="Additional details">{e.additionalDetails || '-'}</Item>
                </div>
            </div>

            <div className="card">
                <h2>Map</h2>
                <MapView markers={markers} />
            </div>

            <div className="grid grid-2">
                <div className="card">
                    <h2>Ambulance assignment</h2>
                    {assignment ? (
                        <div className="detail-grid">
                            <Item label="Assignment ID">#{assignment.assignmentId}</Item>
                            <Item label="Ambulance ID">#{assignment.ambulanceId}</Item>
                            <Item label="Distance">
                                {assignment.distanceInKm != null
                                    ? `${Number(assignment.distanceInKm).toFixed(2)} km`
                                    : '-'}
                            </Item>
                            <Item label="Status">
                                <StatusBadge value={assignment.assignmentStatus} />
                            </Item>
                            <Item label="Assigned at">{formatDateTime(assignment.assignedAt)}</Item>
                            <Item label="Completed at">{formatDateTime(assignment.completedAt)}</Item>
                        </div>
                    ) : (
                        <EmptyState message="No ambulance assigned yet."/>
                    )}
                </div>

                <div className="card">
                    <h2>Tracking</h2>
                    {tracking ? (
                        <div className="detail-grid">
                            <Item label="Ambulance ID">#{tracking.ambulanceId}</Item>
                            <Item label="Trip status">
                                <StatusBadge value={tracking.tripStatus} />
                            </Item>
                            <Item label="Location">
                                {formatCoord(tracking.latitude)}, {formatCoord(tracking.longitude)}
                            </Item>
                            <Item label="Updated">{formatDateTime(tracking.updatedAt)}</Item>
                        </div>
                    ) : (
                        <EmptyState message="Tracking has not started yet."/>
                    )}
                </div>
            </div>

            <div className="card">
                <h2>Notifications</h2>
                {notifications.length === 0 ? (
                    <EmptyState message="No notifications found."/>
                ) : (
                    <div className="table-wrap">
                        <table>
                            <thead>
                            <tr>
                                <th>Type</th>
                                <th>Message</th>
                                <th>Status</th>
                                <th>Sent at</th>
                            </tr>
                            </thead>
                            <tbody>
                            {notifications.map((n) => (
                                <tr key={n.id}>
                                    <td>{prettyEnum(n.notificationType)}</td>
                                    <td>{n.message}</td>
                                    <td>
                                        <StatusBadge value={n.notificationStatus} />
                                    </td>
                                    <td>{formatDateTime(n.sentAt)}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>
        </div>
    )
}