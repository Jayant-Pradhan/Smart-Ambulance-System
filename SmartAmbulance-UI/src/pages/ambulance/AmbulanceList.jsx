import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import {
    getAllAmbulances,
    updateAmbulanceStatus,
    updateAmbulanceLocation,
} from '../../api/ambulanceApi'
import { getErrorMessage } from '../../utils/errors'
import { AMBULANCE_STATUSES, AMBULANCE_TYPES } from '../../utils/constants'
import { formatCoord, prettyEnum, yesNo } from '../../utils/format'
import { useToast } from '../../components/Toast'
import Loader from '../../components/Loader'
import EmptyState from '../../components/EmptyState'
import StatusBadge from '../../components/StatusBadge'
import Modal from '../../components/Modal'
import MapPicker from '../../components/MapPicker'

export default function AmbulanceList() {
    const toast = useToast()
    const [ambulances, setAmbulances] = useState([])
    const [loading, setLoading] = useState(true)
    const [status, setStatus] = useState('')
    const [type, setType] = useState('')

    // status modal
    const [statusTarget, setStatusTarget] = useState(null)
    const [newStatus, setNewStatus] = useState('AVAILABLE')

    // location modal
    const [locTarget, setLocTarget] = useState(null)
    const [lat, setLat] = useState('')
    const [lng, setLng] = useState('')

    const [saving, setSaving] = useState(false)

    async function load() {
        setLoading(true)
        try {
            setAmbulances(await getAllAmbulances())
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

    function replace(updated) {
        setAmbulances((list) => list.map((a) => (a.id === updated.id ? updated : a)))
    }

    function openStatus(a) {
        setStatusTarget(a)
        setNewStatus(a.status)
    }

    function openLocation(a) {
        setLocTarget(a)
        setLat(a.latitude != null ? String(a.latitude) : '')
        setLng(a.longitude != null ? String(a.longitude) : '')
    }

    async function saveStatus() {
        setSaving(true)
        try {
            replace(await updateAmbulanceStatus(statusTarget.id, newStatus))
            toast.success('Status update ho gaya')
            setStatusTarget(null)
        } catch (err) {
            toast.error(getErrorMessage(err))
        } finally {
            setSaving(false)
        }
    }

    async function saveLocation() {
        if (lat === '' || lng === '') {
            toast.error('Map par click karke location select karo')
            return
        }
        setSaving(true)
        try {
            replace(await updateAmbulanceLocation(locTarget.id, Number(lat), Number(lng)))
            toast.success('Location update ho gayi')
            setLocTarget(null)
        } catch (err) {
            toast.error(getErrorMessage(err))
        } finally {
            setSaving(false)
        }
    }

    const filtered = ambulances
        .filter((a) => !status || a.status === status)
        .filter((a) => !type || a.ambulanceType === type)
        .sort((a, b) => a.id - b.id)

    return (
        <div>
            <div className="page-header">
                <div>
                    <h1>Ambulances</h1>
                    <p>Registered ambulances and their status</p>
                </div>
                <div className="btn-row">
                    <button className="btn btn-secondary" onClick={load}>
                        Refresh
                    </button>
                    <Link to="/ambulance/new" className="btn">
                        + Register Ambulance
                    </Link>
                </div>
            </div>

            <div className="card">
                <div className="filters">
                    <select value={status} onChange={(e) => setStatus(e.target.value)}>
                        <option value="">All statuses</option>
                        {AMBULANCE_STATUSES.map((s) => (
                            <option key={s} value={s}>
                                {prettyEnum(s)}
                            </option>
                        ))}
                    </select>
                    <select value={type} onChange={(e) => setType(e.target.value)}>
                        <option value="">All types</option>
                        {AMBULANCE_TYPES.map((t) => (
                            <option key={t} value={t}>
                                {prettyEnum(t)}
                            </option>
                        ))}
                    </select>
                </div>

                {loading ? (
                    <Loader />
                ) : filtered.length === 0 ? (
                    <EmptyState message="Koi ambulance nahi mili." />
                ) : (
                    <div className="table-wrap">
                        <table>
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Reg. No.</th>
                                <th>Type</th>
                                <th>Capacity</th>
                                <th>Oxygen</th>
                                <th>Ventilator</th>
                                <th>Cardiac</th>
                                <th>Location</th>
                                <th>Status</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            {filtered.map((a) => (
                                <tr key={a.id}>
                                    <td>#{a.id}</td>
                                    <td>{a.registrationNumber}</td>
                                    <td>{prettyEnum(a.ambulanceType)}</td>
                                    <td>{a.patientCapacity}</td>
                                    <td>{yesNo(a.hasOxygen)}</td>
                                    <td>{yesNo(a.hasVentilator)}</td>
                                    <td>{yesNo(a.hasCardiacMonitor)}</td>
                                    <td>
                                        {formatCoord(a.latitude)}, {formatCoord(a.longitude)}
                                    </td>
                                    <td>
                                        <StatusBadge value={a.status} />
                                    </td>
                                    <td>
                                        <div className="btn-row">
                                            <button className="btn btn-secondary btn-sm" onClick={() => openStatus(a)}>
                                                Status
                                            </button>
                                            <button className="btn btn-secondary btn-sm" onClick={() => openLocation(a)}>
                                                Location
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>

            {statusTarget && (
                <Modal title={`Ambulance #${statusTarget.id} ka status`} onClose={() => setStatusTarget(null)}>
                    <div className="form-group">
                        <label>New status</label>
                        <select value={newStatus} onChange={(e) => setNewStatus(e.target.value)}>
                            {AMBULANCE_STATUSES.map((s) => (
                                <option key={s} value={s}>
                                    {prettyEnum(s)}
                                </option>
                            ))}
                        </select>
                    </div>
                    <div className="btn-row" style={{ marginTop: 16 }}>
                        <button className="btn" onClick={saveStatus} disabled={saving}>
                            {saving ? 'Saving...' : 'Save'}
                        </button>
                        <button className="btn btn-secondary" onClick={() => setStatusTarget(null)}>
                            Cancel
                        </button>
                    </div>
                </Modal>
            )}

            {locTarget && (
                <Modal title={`Ambulance #${locTarget.id} ki location`} onClose={() => setLocTarget(null)}>
                    <p className="muted" style={{ marginBottom: 10, fontSize: 13 }}>
                        {lat !== '' ? `Lat: ${formatCoord(lat)}, Lng: ${formatCoord(lng)}` : 'Map par click karo'}
                    </p>
                    <MapPicker
                        latitude={lat}
                        longitude={lng}
                        onPick={(la, lo) => {
                            setLat(la.toFixed(6))
                            setLng(lo.toFixed(6))
                        }}
                    />
                    <div className="btn-row" style={{ marginTop: 16 }}>
                        <button className="btn" onClick={saveLocation} disabled={saving}>
                            {saving ? 'Saving...' : 'Save'}
                        </button>
                        <button className="btn btn-secondary" onClick={() => setLocTarget(null)}>
                            Cancel
                        </button>
                    </div>
                </Modal>
            )}
        </div>
    )
}