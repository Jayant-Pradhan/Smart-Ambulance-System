import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { getAllHospitals, createHospital, deactivateHospital } from '../../api/hospitalApi'
import { getErrorMessage } from '../../utils/errors'
import { formatCoord } from '../../utils/format'
import { useToast } from '../../components/Toast'
import Loader from '../../components/Loader'
import EmptyState from '../../components/EmptyState'
import Modal from '../../components/Modal'
import MapPicker from '../../components/MapPicker'

const initial = { name: '', address: '', city: '', latitude: '', longitude: '' }

export default function HospitalList() {
    const toast = useToast()
    const [hospitals, setHospitals] = useState([])
    const [loading, setLoading] = useState(true)
    const [showForm, setShowForm] = useState(false)
    const [form, setForm] = useState(initial)
    const [saving, setSaving] = useState(false)

    async function load() {
        setLoading(true)
        try {
            setHospitals(await getAllHospitals())
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

    const set = (name, value) => setForm((f) => ({ ...f, [name]: value }))

    async function handleCreate(ev) {
        ev.preventDefault()
        if (!form.name.trim() || !form.address.trim()) {
            toast.error('Name and address are required')
            return
        }
        if (form.latitude === '' || form.longitude === '') {
            toast.error('Click on the map to select a location')
            return
        }
        setSaving(true)
        try {
            await createHospital({
                name: form.name.trim(),
                address: form.address.trim(),
                city: form.city.trim(),
                latitude: Number(form.latitude),
                longitude: Number(form.longitude),
            })
            toast.success('Hospital added successfully')
            setShowForm(false)
            setForm(initial)
            load()
        } catch (err) {
            toast.error(getErrorMessage(err))
        } finally {
            setSaving(false)
        }
    }

    async function handleDeactivate(h) {
        if (!window.confirm(`Do you want to deactivate ${h.name}?`)) return
        try {
            await deactivateHospital(h.id)
            toast.success('Hospital deactivated successfully')
            load()
        } catch (err) {
            toast.error(getErrorMessage(err))
        }
    }

    return (
        <div>
            <div className="page-header">
                <div>
                    <h1>Hospitals</h1>
                    <p>Hospitals, departments and beds</p>
                </div>
                <div className="btn-row">
                    <button className="btn btn-secondary" onClick={load}>
                        Refresh
                    </button>
                    <button className="btn" onClick={() => setShowForm(true)}>
                        + Add Hospital
                    </button>
                </div>
            </div>

            <div className="card">
                {loading ? (
                    <Loader />
                ) : hospitals.length === 0 ? (
                    <EmptyState message="No hospitals available yet." />
                ) : (
                    <div className="table-wrap">
                        <table>
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>City</th>
                                <th>Address</th>
                                <th>Location</th>
                                <th>Active</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            {hospitals.map((h) => (
                                <tr key={h.id}>
                                    <td>#{h.id}</td>
                                    <td>
                                        <Link to={`/hospital/${h.id}`}>{h.name}</Link>
                                    </td>
                                    <td>{h.city || '-'}</td>
                                    <td>{h.address}</td>
                                    <td>
                                        {formatCoord(h.latitude)}, {formatCoord(h.longitude)}
                                    </td>
                                    <td>
                      <span className={`badge ${h.active ? 'badge-success' : 'badge-danger'}`}>
                        {h.active ? 'Active' : 'Inactive'}
                      </span>
                                    </td>
                                    <td>
                                        <div className="btn-row">
                                            <Link to={`/hospital/${h.id}`} className="btn btn-secondary btn-sm">
                                                Open
                                            </Link>
                                            {h.active && (
                                                <button
                                                    className="btn btn-danger btn-sm"
                                                    onClick={() => handleDeactivate(h)}
                                                >
                                                    Deactivate
                                                </button>
                                            )}
                                        </div>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>

            {showForm && (
                <Modal title="Add Hospital" onClose={() => setShowForm(false)}>
                    <form onSubmit={handleCreate}>
                        <div className="grid" style={{ gap: 12 }}>
                            <div className="form-group">
                                <label>Name *</label>
                                <input value={form.name} onChange={(e) => set('name', e.target.value)} />
                            </div>
                            <div className="form-group">
                                <label>Address *</label>
                                <input value={form.address} onChange={(e) => set('address', e.target.value)} />
                            </div>
                            <div className="form-group">
                                <label>City</label>
                                <input value={form.city} onChange={(e) => set('city', e.target.value)} />
                            </div>
                            <div className="form-group">
                                <label>
                                    Location *{' '}
                                    <span className="muted">
                    {form.latitude !== ''
                        ? `(${formatCoord(form.latitude)}, ${formatCoord(form.longitude)})`
                        : '(click on the map)'}
                  </span>
                                </label>
                                <MapPicker
                                    latitude={form.latitude}
                                    longitude={form.longitude}
                                    onPick={(la, lo) => {
                                        set('latitude', la.toFixed(6))
                                        set('longitude', lo.toFixed(6))
                                    }}
                                />
                            </div>
                        </div>
                        <div className="btn-row" style={{ marginTop: 16 }}>
                            <button type="submit" className="btn" disabled={saving}>
                                {saving ? 'Saving...' : 'Save'}
                            </button>
                            <button type="button" className="btn btn-secondary" onClick={() => setShowForm(false)}>
                                Cancel
                            </button>
                        </div>
                    </form>
                </Modal>
            )}
        </div>
    )
}