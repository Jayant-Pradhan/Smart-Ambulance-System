import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { getAllTracking } from '../../api/trackingApi'
import { getErrorMessage } from '../../utils/errors'
import { TRIP_STATUSES } from '../../utils/constants'
import { formatCoord, formatDateTime, prettyEnum } from '../../utils/format'
import { useToast } from '../../components/Toast'
import Loader from '../../components/Loader'
import EmptyState from '../../components/EmptyState'
import StatusBadge from '../../components/StatusBadge'
import MapView from '../../components/MapView'

export default function TrackingList() {
    const toast = useToast()
    const [items, setItems] = useState([])
    const [loading, setLoading] = useState(true)
    const [status, setStatus] = useState('')

    async function load(showLoader = true) {
        if (showLoader) setLoading(true)
        try {
            setItems(await getAllTracking())
        } catch (err) {
            toast.error(getErrorMessage(err))
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        load()
        const timer = setInterval(() => load(false), 10000)
        return () => clearInterval(timer)
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])

    const filtered = items
        .filter((t) => !status || t.tripStatus === status)
        .sort((a, b) => b.id - a.id)

    const markers = filtered.map((t) => ({
        lat: t.latitude,
        lng: t.longitude,
        label: `Ambulance #${t.ambulanceId} (Emergency #${t.emergencyId})`,
    }))

    return (
        <div>
            <div className="page-header">
                <div>
                    <h1>Tracking</h1>
                    <p>Ambulance trips (auto-refreshes every 10 seconds)</p>
                </div>
                <button className="btn btn-secondary" onClick={() => load()}>
                    Refresh
                </button>
            </div>

            <div className="card">
                <h2>Live map</h2>
                <MapView markers={markers} zoom={11} />
            </div>

            <div className="card">
                <div className="filters">
                    <select value={status} onChange={(e) => setStatus(e.target.value)}>
                        <option value="">All trip statuses</option>
                        {TRIP_STATUSES.map((s) => (
                            <option key={s} value={s}>
                                {prettyEnum(s)}
                            </option>
                        ))}
                    </select>
                </div>

                {loading ? (
                    <Loader />
                ) : filtered.length === 0 ? (
                    <EmptyState message="No tracking data available yet." />
                ) : (
                    <div className="table-wrap">
                        <table>
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Emergency</th>
                                <th>Ambulance</th>
                                <th>Location</th>
                                <th>Trip status</th>
                                <th>Updated</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            {filtered.map((t) => (
                                <tr key={t.id}>
                                    <td>#{t.id}</td>
                                    <td>
                                        <Link to={`/emergency/${t.emergencyId}`}>#{t.emergencyId}</Link>
                                    </td>
                                    <td>#{t.ambulanceId}</td>
                                    <td>
                                        {formatCoord(t.latitude)}, {formatCoord(t.longitude)}
                                    </td>
                                    <td>
                                        <StatusBadge value={t.tripStatus} />
                                    </td>
                                    <td>{formatDateTime(t.updatedAt)}</td>
                                    <td>
                                        <Link to={`/tracking/${t.emergencyId}`} className="btn btn-secondary btn-sm">
                                            Open
                                        </Link>
                                    </td>
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