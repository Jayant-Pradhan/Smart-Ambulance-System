import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { getAllEmergencies } from '../../api/emergencyApi'
import { getErrorMessage } from '../../utils/errors'
import { EMERGENCY_STATUSES, EMERGENCY_TYPES } from '../../utils/constants'
import { formatDateTime, prettyEnum } from '../../utils/format'
import { useToast } from '../../components/Toast'
import Loader from '../../components/Loader'
import EmptyState from '../../components/EmptyState'
import StatusBadge from '../../components/StatusBadge'

export default function EmergencyList() {
    const toast = useToast()
    const [emergencies, setEmergencies] = useState([])
    const [loading, setLoading] = useState(true)
    const [status, setStatus] = useState('')
    const [type, setType] = useState('')
    const [search, setSearch] = useState('')

    async function load() {
        setLoading(true)
        try {
            setEmergencies(await getAllEmergencies())
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

    const filtered = emergencies
        .filter((e) => !status || e.status === status)
        .filter((e) => !type || e.emergencyType === type)
        .filter((e) => {
            if (!search) return true
            const s = search.toLowerCase()
            return (
                String(e.id).includes(s) ||
                (e.callerName || '').toLowerCase().includes(s) ||
                (e.callerMobile || '').includes(s)
            )
        })
        .sort((a, b) => b.id - a.id)

    return (
        <div>
            <div className="page-header">
                <div>
                    <h1>Emergencies</h1>
                    <p>All emergency requests</p>
                </div>
                <div className="btn-row">
                    <button className="btn btn-secondary" onClick={load}>
                        Refresh
                    </button>
                    <Link to="/emergency/new" className="btn btn-danger">
                        + New Emergency
                    </Link>
                </div>
            </div>

            <div className="card">
                <div className="filters">
                    <input
                        placeholder="Search ID, name or mobile"
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                    />
                    <select value={status} onChange={(e) => setStatus(e.target.value)}>
                        <option value="">All statuses</option>
                        {EMERGENCY_STATUSES.map((s) => (
                            <option key={s} value={s}>
                                {prettyEnum(s)}
                            </option>
                        ))}
                    </select>
                    <select value={type} onChange={(e) => setType(e.target.value)}>
                        <option value="">All types</option>
                        {EMERGENCY_TYPES.map((t) => (
                            <option key={t} value={t}>
                                {prettyEnum(t)}
                            </option>
                        ))}
                    </select>
                </div>

                {loading ? (
                    <Loader />
                ) : filtered.length === 0 ? (
                    <EmptyState message="No emergency found."/>
                ) : (
                    <div className="table-wrap">
                        <table>
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Caller</th>
                                <th>Mobile</th>
                                <th>Type</th>
                                <th>Patients</th>
                                <th>Status</th>
                                <th>Created</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            {filtered.map((e) => (
                                <tr key={e.id}>
                                    <td>#{e.id}</td>
                                    <td>{e.callerName}</td>
                                    <td>{e.callerMobile}</td>
                                    <td>{prettyEnum(e.emergencyType)}</td>
                                    <td>{e.patientCount}</td>
                                    <td>
                                        <StatusBadge value={e.status} />
                                    </td>
                                    <td>{formatDateTime(e.createdAt)}</td>
                                    <td>
                                        <Link to={`/emergency/${e.id}`} className="btn btn-secondary btn-sm">
                                            View
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