import { useCallback, useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import {
    getHospitalById,
    getDepartmentsByHospital,
    createDepartment,
    deactivateDepartment,
    getBedsByHospital,
    createBed,
    reserveBed,
    occupyBed,
    releaseBed,
    markBedOutOfService,
    markBedAvailable,
} from '../../api/hospitalApi'
import { getErrorMessage } from '../../utils/errors'
import { BED_TYPES } from '../../utils/constants'
import { formatCoord, prettyEnum } from '../../utils/format'
import { useToast } from '../../components/Toast'
import Loader from '../../components/Loader'
import EmptyState from '../../components/EmptyState'
import StatusBadge from '../../components/StatusBadge'
import Modal from '../../components/Modal'
import MapView from '../../components/MapView'

// Bed ke status ke hisaab se kaun se buttons dikhane hain
const BED_ACTIONS = {
    AVAILABLE: [
        { label: 'Reserve', fn: reserveBed },
        { label: 'Out of service', fn: markBedOutOfService },
    ],
    RESERVED: [
        { label: 'Occupy', fn: occupyBed },
        { label: 'Release', fn: releaseBed },
    ],
    OCCUPIED: [{ label: 'Release', fn: releaseBed }],
    OUT_OF_SERVICE: [{ label: 'Mark available', fn: markBedAvailable }],
}

export default function HospitalDetail() {
    const { id } = useParams()
    const toast = useToast()
    const [hospital, setHospital] = useState(null)
    const [departments, setDepartments] = useState([])
    const [beds, setBeds] = useState([])
    const [loading, setLoading] = useState(true)

    const [deptName, setDeptName] = useState('')
    const [showBedForm, setShowBedForm] = useState(false)
    const [bedForm, setBedForm] = useState({ roomNumber: '', bedNumber: '', bedType: 'GENERAL' })
    const [saving, setSaving] = useState(false)

    const load = useCallback(async () => {
        try {
            setHospital(await getHospitalById(id))
        } catch (err) {
            toast.error(getErrorMessage(err))
            setLoading(false)
            return
        }
        const [d, b] = await Promise.allSettled([getDepartmentsByHospital(id), getBedsByHospital(id)])
        setDepartments(d.status === 'fulfilled' ? d.value : [])
        setBeds(b.status === 'fulfilled' ? b.value : [])
        setLoading(false)
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [id])

    useEffect(() => {
        load()
    }, [load])

    async function addDepartment(ev) {
        ev.preventDefault()
        if (!deptName.trim()) {
            toast.error('Enter the department name')
            return
        }
        try {
            await createDepartment({ name: deptName.trim(), hospitalId: Number(id) })
            toast.success('Department added successfully')
            setDeptName('')
            load()
        } catch (err) {
            toast.error(getErrorMessage(err))
        }
    }

    async function removeDepartment(d) {
        if (!window.confirm('Do you want to deactivate ${d.name}?')) return
        try {
            await deactivateDepartment(d.id)
            toast.success('Department deactivated successfully')
            load()
        } catch (err) {
            toast.error(getErrorMessage(err))
        }
    }

    async function addBed(ev) {
        ev.preventDefault()
        if (!bedForm.roomNumber.trim() || !bedForm.bedNumber.trim()) {
            toast.error('Enter room number and bed number')
            return
        }
        setSaving(true)
        try {
            await createBed({
                roomNumber: bedForm.roomNumber.trim(),
                bedNumber: bedForm.bedNumber.trim(),
                bedType: bedForm.bedType,
                status: 'AVAILABLE',
                hospitalId: Number(id),
            })
            toast.success('Bed added successfully')
            setShowBedForm(false)
            setBedForm({ roomNumber: '', bedNumber: '', bedType: 'GENERAL' })
            load()
        } catch (err) {
            toast.error(getErrorMessage(err))
        } finally {
            setSaving(false)
        }
    }

    async function bedAction(bed, action) {
        try {
            const updated = await action.fn(bed.id)
            setBeds((list) => list.map((b) => (b.id === updated.id ? updated : b)))
            toast.success(`Bed ${action.label.toLowerCase()} ho gaya`)
        } catch (err) {
            toast.error(getErrorMessage(err))
        }
    }

    if (loading) return <Loader />
    if (!hospital) {
        return (
            <div>
                <EmptyState message="Hospital not found."/>
                <Link to="/hospital">Go back</Link>
            </div>
        )
    }

    const availableCount = beds.filter((b) => b.status === 'AVAILABLE').length

    return (
        <div>
            <div className="page-header">
                <div>
                    <h1>{hospital.name}</h1>
                    <p>
                        {hospital.address}
                        {hospital.city ? `, ${hospital.city}` : ''}
                    </p>
                </div>
                <Link to="/hospital" className="btn btn-secondary">
                    Back
                </Link>
            </div>

            <div className="grid grid-3" style={{ marginBottom: 20 }}>
                <div className="stat">
                    <div className="label">Total beds</div>
                    <div className="value">{beds.length}</div>
                </div>
                <div className="stat">
                    <div className="label">Available beds</div>
                    <div className="value" style={{ color: 'var(--success)' }}>
                        {availableCount}
                    </div>
                </div>
                <div className="stat">
                    <div className="label">Departments</div>
                    <div className="value">{departments.length}</div>
                </div>
            </div>

            <div className="card">
                <h2>
                    Location ({formatCoord(hospital.latitude)}, {formatCoord(hospital.longitude)})
                </h2>
                <MapView
                    markers={[{ lat: hospital.latitude, lng: hospital.longitude, label: hospital.name }]}
                    zoom={14}
                />
            </div>

            <div className="card">
                <h2>Departments</h2>
                <form onSubmit={addDepartment} className="filters">
                    <input
                        placeholder="New department (e.g., Cardiology)"
                        value={deptName}
                        onChange={(e) => setDeptName(e.target.value)}
                    />
                    <button type="submit" className="btn btn-sm">
                        + Add
                    </button>
                </form>
                {departments.length === 0 ? (
                    <EmptyState message="No departments available."/>
                ) : (
                    <div className="table-wrap">
                        <table>
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Active</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            {departments.map((d) => (
                                <tr key={d.id}>
                                    <td>#{d.id}</td>
                                    <td>{d.name}</td>
                                    <td>
                      <span className={`badge ${d.active ? 'badge-success' : 'badge-danger'}`}>
                        {d.active ? 'Active' : 'Inactive'}
                      </span>
                                    </td>
                                    <td>
                                        {d.active && (
                                            <button className="btn btn-danger btn-sm" onClick={() => removeDepartment(d)}>
                                                Deactivate
                                            </button>
                                        )}
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>

            <div className="card">
                <div className="page-header" style={{ marginBottom: 12 }}>
                    <h2 style={{ marginBottom: 0 }}>Beds</h2>
                    <button className="btn btn-sm" onClick={() => setShowBedForm(true)}>
                        + Add Bed
                    </button>
                </div>
                {beds.length === 0 ? (
                    <EmptyState message="No beds available."/>
                ) : (
                    <div className="table-wrap">
                        <table>
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Room</th>
                                <th>Bed</th>
                                <th>Type</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            {beds.map((b) => (
                                <tr key={b.id}>
                                    <td>#{b.id}</td>
                                    <td>{b.roomNumber}</td>
                                    <td>{b.bedNumber}</td>
                                    <td>{prettyEnum(b.bedType)}</td>
                                    <td>
                                        <StatusBadge value={b.status} />
                                    </td>
                                    <td>
                                        <div className="btn-row">
                                            {(BED_ACTIONS[b.status] || []).map((action) => (
                                                <button
                                                    key={action.label}
                                                    className="btn btn-secondary btn-sm"
                                                    onClick={() => bedAction(b, action)}
                                                >
                                                    {action.label}
                                                </button>
                                            ))}
                                        </div>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>

            {showBedForm && (
                <Modal title="Add Bed" onClose={() => setShowBedForm(false)}>
                    <form onSubmit={addBed}>
                        <div className="grid" style={{ gap: 12 }}>
                            <div className="form-group">
                                <label>Room number *</label>
                                <input
                                    value={bedForm.roomNumber}
                                    onChange={(e) => setBedForm({ ...bedForm, roomNumber: e.target.value })}
                                />
                            </div>
                            <div className="form-group">
                                <label>Bed number *</label>
                                <input
                                    value={bedForm.bedNumber}
                                    onChange={(e) => setBedForm({ ...bedForm, bedNumber: e.target.value })}
                                />
                            </div>
                            <div className="form-group">
                                <label>Bed type *</label>
                                <select
                                    value={bedForm.bedType}
                                    onChange={(e) => setBedForm({ ...bedForm, bedType: e.target.value })}
                                >
                                    {BED_TYPES.map((t) => (
                                        <option key={t} value={t}>
                                            {prettyEnum(t)}
                                        </option>
                                    ))}
                                </select>
                            </div>
                        </div>
                        <div className="btn-row" style={{ marginTop: 16 }}>
                            <button type="submit" className="btn" disabled={saving}>
                                {saving ? 'Saving...' : 'Save'}
                            </button>
                            <button type="button" className="btn btn-secondary" onClick={() => setShowBedForm(false)}>
                                Cancel
                            </button>
                        </div>
                    </form>
                </Modal>
            )}
        </div>
    )
}