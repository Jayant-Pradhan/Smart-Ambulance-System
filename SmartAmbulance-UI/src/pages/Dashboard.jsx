import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { getAllEmergencies } from '../api/emergencyApi'
import { getAllAmbulances } from '../api/ambulanceApi'
import { getAllAssignments } from '../api/assignmentApi'
import { getAllHospitals } from '../api/hospitalApi'
import { getAllNotifications } from '../api/notificationApi'
import Loader from '../components/Loader'
import EmptyState from '../components/EmptyState'
import StatusBadge from '../components/StatusBadge'
import { formatDateTime, prettyEnum } from '../utils/format'

export default function Dashboard() {
    const [loading, setLoading] = useState(true)
    const [data, setData] = useState({
        emergencies: [],
        ambulances: [],
        assignments: [],
        hospitals: [],
        notifications: [],
    })
    const [failed, setFailed] = useState([])

    useEffect(() => {
        async function load() {
            const calls = [
                ['Emergency', getAllEmergencies()],
                ['Ambulance', getAllAmbulances()],
                ['Assignment', getAllAssignments()],
                ['Hospital', getAllHospitals()],
                ['Notification', getAllNotifications()],
            ]
            const results = await Promise.allSettled(calls.map((c) => c[1]))
            const pick = (i) => (results[i].status === 'fulfilled' ? results[i].value : [])

            setData({
                emergencies: pick(0),
                ambulances: pick(1),
                assignments: pick(2),
                hospitals: pick(3),
                notifications: pick(4),
            })
            setFailed(calls.filter((_, i) => results[i].status === 'rejected').map((c) => c[0]))
            setLoading(false)
        }
        load()
    }, [])

    if (loading) return <Loader />

    const { emergencies, ambulances, assignments, hospitals, notifications } = data
    const activeEmergencies = emergencies.filter(
        (e) => e.status !== 'COMPLETED' && e.status !== 'CANCELLED',
    )
    const availableAmbulances = ambulances.filter((a) => a.status === 'AVAILABLE')
    const sentNotifications = notifications.filter((n) => n.notificationStatus === 'SENT')
    const recent = [...emergencies].sort((a, b) => b.id - a.id).slice(0, 5)

    return (
        <div>
            <div className="page-header">
                <div>
                    <h1>Dashboard</h1>
                    <p>Overview of the Smart Ambulance System</p>
                </div>
                <Link to="/emergency/new" className="btn btn-danger">
                    + New Emergency
                </Link>
            </div>

            {failed.length > 0 && (
                <div className="card" style={{ borderColor: 'var(--warning)' }}>
          <span className="muted">
            These services are not responding: <b>{failed.join(', ')}</b>. Please check the backend.
          </span>
                </div>
            )}

            <div className="grid grid-4" style={{ marginBottom: 20 }}>
                <div className="stat">
                    <div className="label">Total Emergencies</div>
                    <div className="value">{emergencies.length}</div>
                </div>
                <div className="stat">
                    <div className="label">Active Emergencies</div>
                    <div className="value" style={{ color: 'var(--danger)' }}>
                        {activeEmergencies.length}
                    </div>
                </div>
                <div className="stat">
                    <div className="label">Available Ambulances</div>
                    <div className="value" style={{ color: 'var(--success)' }}>
                        {availableAmbulances.length}
                        <span className="muted" style={{ fontSize: 16 }}> / {ambulances.length}</span>
                    </div>
                </div>
                <div className="stat">
                    <div className="label">Assignments</div>
                    <div className="value">{assignments.length}</div>
                </div>
                <div className="stat">
                    <div className="label">Hospitals</div>
                    <div className="value">{hospitals.length}</div>
                </div>
                <div className="stat">
                    <div className="label">Notifications Sent</div>
                    <div className="value">
                        {sentNotifications.length}
                        <span className="muted" style={{ fontSize: 16 }}> / {notifications.length}</span>
                    </div>
                </div>
            </div>

            <div className="card">
                <h2>Recent Emergencies</h2>
                {recent.length === 0 ? (
                    <EmptyState message="No emergencies found yet." />
                ) : (
                    <div className="table-wrap">
                        <table>
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Caller</th>
                                <th>Type</th>
                                <th>Status</th>
                                <th>Created</th>
                            </tr>
                            </thead>
                            <tbody>
                            {recent.map((e) => (
                                <tr key={e.id}>
                                    <td>
                                        <Link to={`/emergency/${e.id}`}>#{e.id}</Link>
                                    </td>
                                    <td>{e.callerName}</td>
                                    <td>{prettyEnum(e.emergencyType)}</td>
                                    <td>
                                        <StatusBadge value={e.status} />
                                    </td>
                                    <td>{formatDateTime(e.createdAt)}</td>
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