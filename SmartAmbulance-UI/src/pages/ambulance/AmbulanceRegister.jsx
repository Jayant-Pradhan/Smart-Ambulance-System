import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { registerAmbulance } from '../../api/ambulanceApi'
import { getErrorMessage } from '../../utils/errors'
import { AMBULANCE_TYPES } from '../../utils/constants'
import { prettyEnum, formatCoord } from '../../utils/format'
import { useToast } from '../../components/Toast'
import MapPicker from '../../components/MapPicker'

const initial = {
    registrationNumber: '',
    ambulanceType: 'BASIC',
    patientCapacity: 1,
    hasOxygen: false,
    hasVentilator: false,
    hasCardiacMonitor: false,
    latitude: '',
    longitude: '',
}

export default function AmbulanceRegister() {
    const toast = useToast()
    const navigate = useNavigate()
    const [form, setForm] = useState(initial)
    const [errors, setErrors] = useState({})
    const [saving, setSaving] = useState(false)

    const set = (name, value) => setForm((f) => ({ ...f, [name]: value }))

    function pickLocation(lat, lng) {
        setForm((f) => ({ ...f, latitude: lat.toFixed(6), longitude: lng.toFixed(6) }))
    }

    function validate() {
        const e = {}
        if (!/^[A-Za-z0-9 -]{4,20}$/.test(form.registrationNumber))
            e.registrationNumber = 'Registration number must be 4-20 characters long'
        if (!form.patientCapacity || Number(form.patientCapacity) < 1)
            e.patientCapacity = 'Capacity must be at least 1'
        if (form.latitude === '' || form.longitude === '')
            e.location = 'Click on the map to select the starting location'
        return e
    }

    async function handleSubmit(ev) {
        ev.preventDefault()
        const e = validate()
        setErrors(e)
        if (Object.keys(e).length > 0) return

        setSaving(true)
        try {
            const payload = {
                ...form,
                registrationNumber: form.registrationNumber.trim(),
                patientCapacity: Number(form.patientCapacity),
                latitude: Number(form.latitude),
                longitude: Number(form.longitude),
            }
            const created = await registerAmbulance(payload)
            toast.success(`Ambulance #${created.id} registered successfully`)
            navigate('/ambulance')
        } catch (err) {
            toast.error(getErrorMessage(err))
        } finally {
            setSaving(false)
        }
    }

    return (
        <div>
            <div className="page-header">
                <div>
                    <h1>Register Ambulance</h1>
                    <p>Add a new ambulance to the system (default status: Offline)</p>
                </div>
            </div>

            <form onSubmit={handleSubmit}>
                <div className="card">
                    <h2>Ambulance details</h2>
                    <div className="form-grid">
                        <div className="form-group">
                            <label>Registration number *</label>
                            <input
                                value={form.registrationNumber}
                                onChange={(e) => set('registrationNumber', e.target.value)}
                                placeholder="OD-02-AB-1234"
                            />
                            {errors.registrationNumber && (
                                <span className="field-error">{errors.registrationNumber}</span>
                            )}
                        </div>
                        <div className="form-group">
                            <label>Ambulance type *</label>
                            <select value={form.ambulanceType} onChange={(e) => set('ambulanceType', e.target.value)}>
                                {AMBULANCE_TYPES.map((t) => (
                                    <option key={t} value={t}>
                                        {prettyEnum(t)}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div className="form-group">
                            <label>Patient capacity *</label>
                            <input
                                type="number"
                                min="1"
                                value={form.patientCapacity}
                                onChange={(e) => set('patientCapacity', e.target.value)}
                            />
                            {errors.patientCapacity && (
                                <span className="field-error">{errors.patientCapacity}</span>
                            )}
                        </div>
                    </div>
                </div>

                <div className="card">
                    <h2>Equipment</h2>
                    <div className="grid grid-3">
                        <div className="checkbox-row">
                            <input
                                id="oxygen"
                                type="checkbox"
                                checked={form.hasOxygen}
                                onChange={(e) => set('hasOxygen', e.target.checked)}
                            />
                            <label htmlFor="oxygen">Oxygen</label>
                        </div>
                        <div className="checkbox-row">
                            <input
                                id="ventilator"
                                type="checkbox"
                                checked={form.hasVentilator}
                                onChange={(e) => set('hasVentilator', e.target.checked)}
                            />
                            <label htmlFor="ventilator">Ventilator</label>
                        </div>
                        <div className="checkbox-row">
                            <input
                                id="cardiac"
                                type="checkbox"
                                checked={form.hasCardiacMonitor}
                                onChange={(e) => set('hasCardiacMonitor', e.target.checked)}
                            />
                            <label htmlFor="cardiac">Cardiac monitor</label>
                        </div>
                    </div>
                </div>

                <div className="card">
                    <h2>Starting location *</h2>
                    <p className="muted" style={{ marginBottom: 12, fontSize: 13 }}>
                        {form.latitude !== ''
                            ? `Lat: ${formatCoord(form.latitude)}, Lng: ${formatCoord(form.longitude)}`
                            : 'Click on the map to select a location'}
                    </p>
                    <MapPicker latitude={form.latitude} longitude={form.longitude} onPick={pickLocation} />
                    {errors.location && (
                        <div className="field-error" style={{ marginTop: 8 }}>
                            {errors.location}
                        </div>
                    )}
                </div>

                <div className="btn-row">
                    <button type="submit" className="btn" disabled={saving}>
                        {saving ? 'Registering...' : 'Register Ambulance'}
                    </button>
                    <button type="button" className="btn btn-secondary" onClick={() => navigate('/ambulance')}>
                        Cancel
                    </button>
                </div>
            </form>
        </div>
    )
}