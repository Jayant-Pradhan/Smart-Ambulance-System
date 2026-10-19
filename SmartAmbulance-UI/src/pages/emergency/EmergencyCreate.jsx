import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { createEmergency } from '../../api/emergencyApi'
import { getErrorMessage } from '../../utils/errors'
import { EMERGENCY_TYPES } from '../../utils/constants'
import { prettyEnum, formatCoord } from '../../utils/format'
import { useToast } from '../../components/Toast'
import MapPicker from '../../components/MapPicker'

const initial = {
    callerName: '',
    callerMobile: '',
    callerEmail: '',
    emergencyType: 'ACCIDENT',
    patientCount: 1,
    latitude: '',
    longitude: '',
    unconscious: false,
    breathingDifficulty: false,
    severeBleeding: false,
    trappedInVehicle: false,
    additionalDetails: '',
}

export default function EmergencyCreate() {
    const toast = useToast()
    const navigate = useNavigate()
    const [form, setForm] = useState(initial)
    const [errors, setErrors] = useState({})
    const [saving, setSaving] = useState(false)

    const set = (name, value) => setForm((f) => ({ ...f, [name]: value }))

    function pickLocation(lat, lng) {
        setForm((f) => ({ ...f, latitude: lat.toFixed(6), longitude: lng.toFixed(6) }))
    }

    function useMyLocation() {
        if (!navigator.geolocation) {
            toast.error('Location is not supported in this browser.')
            return
        }
        navigator.geolocation.getCurrentPosition(
            (pos) => pickLocation(pos.coords.latitude, pos.coords.longitude),
            () => toast.error('Location not found. Click on the map to select.'),
        )
    }

    function validate() {
        const e = {}
        if (!form.callerName.trim()) e.callerName = 'Caller name is mandatory'
        if (!/^[6-9][0-9]{9}$/.test(form.callerMobile))
            e.callerMobile = 'Enter a valid 10-digit Indian mobile number'
        if (!/^\S+@\S+\.\S+$/.test(form.callerEmail)) e.callerEmail = 'Enter Valid Mail'
        if (!form.patientCount || Number(form.patientCount) < 1)
            e.patientCount = 'Atleast 1 patient'
        if (form.latitude === '' || form.longitude === '')
            e.location = 'Click on the map to select the location'
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
                callerName: form.callerName.trim(),
                callerEmail: form.callerEmail.trim(),
                patientCount: Number(form.patientCount),
                latitude: Number(form.latitude),
                longitude: Number(form.longitude),
                additionalDetails: form.additionalDetails.trim() || null,
            }
            const created = await createEmergency(payload)
            toast.success(`Emergency #${created.id} create ho gayi`)
            navigate(`/emergency/${created.id}`)
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
                    <h1>New Emergency</h1>
                    <p>As soon as an emergency is created, an email will be sent to the caller and an ambulance will be auto-assigned.</p>
                </div>
            </div>

            <form onSubmit={handleSubmit}>
                <div className="card">
                    <h2>Caller details</h2>
                    <div className="form-grid">
                        <div className="form-group">
                            <label>Caller name *</label>
                            <input value={form.callerName} onChange={(e) => set('callerName', e.target.value)} />
                            {errors.callerName && <span className="field-error">{errors.callerName}</span>}
                        </div>
                        <div className="form-group">
                            <label>Mobile number *</label>
                            <input
                                value={form.callerMobile}
                                maxLength={10}
                                onChange={(e) => set('callerMobile', e.target.value.replace(/\D/g, ''))}
                            />
                            {errors.callerMobile && <span className="field-error">{errors.callerMobile}</span>}
                        </div>
                        <div className="form-group">
                            <label>Email *</label>
                            <input
                                type="email"
                                value={form.callerEmail}
                                onChange={(e) => set('callerEmail', e.target.value)}
                            />
                            {errors.callerEmail && <span className="field-error">{errors.callerEmail}</span>}
                        </div>
                        <div className="form-group">
                            <label>Emergency type *</label>
                            <select value={form.emergencyType} onChange={(e) => set('emergencyType', e.target.value)}>
                                {EMERGENCY_TYPES.map((t) => (
                                    <option key={t} value={t}>
                                        {prettyEnum(t)}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div className="form-group">
                            <label>Patient count *</label>
                            <input
                                type="number"
                                min="1"
                                value={form.patientCount}
                                onChange={(e) => set('patientCount', e.target.value)}
                            />
                            {errors.patientCount && <span className="field-error">{errors.patientCount}</span>}
                        </div>
                    </div>
                </div>

                <div className="card">
                    <h2>Patient condition</h2>
                    <div className="grid grid-2">
                        <div className="checkbox-row">
                            <input
                                id="unconscious"
                                type="checkbox"
                                checked={form.unconscious}
                                onChange={(e) => set('unconscious', e.target.checked)}
                            />
                            <label htmlFor="unconscious">Unconscious</label>
                        </div>
                        <div className="checkbox-row">
                            <input
                                id="breathing"
                                type="checkbox"
                                checked={form.breathingDifficulty}
                                onChange={(e) => set('breathingDifficulty', e.target.checked)}
                            />
                            <label htmlFor="breathing">Breathing difficulty</label>
                        </div>
                        <div className="checkbox-row">
                            <input
                                id="bleeding"
                                type="checkbox"
                                checked={form.severeBleeding}
                                onChange={(e) => set('severeBleeding', e.target.checked)}
                            />
                            <label htmlFor="bleeding">Severe bleeding</label>
                        </div>
                        <div className="checkbox-row">
                            <input
                                id="trapped"
                                type="checkbox"
                                checked={form.trappedInVehicle}
                                onChange={(e) => set('trappedInVehicle', e.target.checked)}
                            />
                            <label htmlFor="trapped">Trapped in vehicle</label>
                        </div>
                    </div>
                    <div className="form-group full" style={{ marginTop: 16 }}>
                        <label>Additional details</label>
                        <textarea
                            value={form.additionalDetails}
                            onChange={(e) => set('additionalDetails', e.target.value)}
                        />
                    </div>
                </div>

                <div className="card">
                    <h2>Location *</h2>
                    <div className="btn-row" style={{ marginBottom: 12 }}>
                        <button type="button" className="btn btn-secondary btn-sm" onClick={useMyLocation}>
                            Meri location use karo
                        </button>
                        <span className="muted" style={{ alignSelf: 'center', fontSize: 13 }}>
              {form.latitude !== ''
                  ? `Lat: ${formatCoord(form.latitude)}, Lng: ${formatCoord(form.longitude)}`
                  : 'Click on the map to select'}
            </span>
                    </div>
                    <MapPicker latitude={form.latitude} longitude={form.longitude} onPick={pickLocation} />
                    {errors.location && (
                        <div className="field-error" style={{ marginTop: 8 }}>
                            {errors.location}
                        </div>
                    )}
                </div>

                <div className="btn-row">
                    <button type="submit" className="btn btn-danger" disabled={saving}>
                        {saving ? 'Creating...' : 'Create Emergency'}
                    </button>
                    <button type="button" className="btn btn-secondary" onClick={() => navigate('/emergency')}>
                        Cancel
                    </button>
                </div>
            </form>
        </div>
    )
}