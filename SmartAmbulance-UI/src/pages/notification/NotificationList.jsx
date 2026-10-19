import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { getAllNotifications } from '../../api/notificationApi'
import { getErrorMessage } from '../../utils/errors'
import { NOTIFICATION_STATUSES } from '../../utils/constants'
import { formatDateTime, prettyEnum } from '../../utils/format'
import { useToast } from '../../components/Toast'
import Loader from '../../components/Loader'
import EmptyState from '../../components/EmptyState'
import StatusBadge from '../../components/StatusBadge'

export default function NotificationList() {
    const toast = useToast()
    const [items, setItems] = useState([])
    const [loading, setLoading] = useState(true)
    const [status, setStatus] = useState('')
    const [search, setSearch] = useState('')

    async function load() {
        setLoading(true)
        try {
            setItems(await getAllNotifications())
        } catch (err) {
            toast.error(getErrorMessage(err))
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        load()
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])

    const filtered = items
        .filter((n) => !status || n.notificationStatus === status)
        .filter((n) => !search || String(n.emergencyId).includes(search.trim()))
        .sort((a, b) => b.id - a.id)

    return (
        <div>
            <div className="page-header">
                <div>
                    <h1>Notifications</h1>
                    <p>Email notifications sent via Kafka events</p>
                </div>
                <button className="btn btn-secondary" onClick={load}>
                    Refresh
                </button>
            </div>

            <div className="card">
                <div className="filters">
                    <input
                        placeholder="Search by Emergency ID"
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                    />
                    <select value={status} onChange={(e) => setStatus(e.target.value)}>
                        <option value="">All statuses</option>
                        {NOTIFICATION_STATUSES.map((s) => (
                            <option key={s} value={s}>
                                {prettyEnum(s)}
                            </option>
                        ))}
                    </select>
                </div>

                {loading ? (
                    <Loader />
                ) : filtered.length === 0 ? (
                    <EmptyState message="No notifications found." />
                ) : (
                    <div className="table-wrap">
                        <table>
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Emergency</th>
                                <th>Type</th>
                                <th>Message</th>
                                <th>Status</th>
                                <th>Created</th>
                                <th>Sent</th>
                            </tr>
                            </thead>
                            <tbody>
                            {filtered.map((n) => (
                                <tr key={n.id}>
                                    <td>#{n.id}</td>
                                    <td>
                                        {n.emergencyId != null ? (
                                            <Link to={`/emergency/${n.emergencyId}`}>#{n.emergencyId}</Link>
                                        ) : (
                                            '-'
                                        )}
                                    </td>
                                    <td>{prettyEnum(n.notificationType)}</td>
                                    <td>{n.message}</td>
                                    <td>
                                        <StatusBadge value={n.notificationStatus} />
                                    </td>
                                    <td>{formatDateTime(n.createdAt)}</td>
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