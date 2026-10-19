import { useCallback, useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import {
    getTrackingByEmergency,
    updateLocation,
    updateTripStatus,
} from '../../api/trackingApi'
import { getEmergencyById } from '../../api/emergencyApi'
import { getErrorMessage } from '../../utils/errors'
import { TRIP_STATUSES } from '../../utils/constants'
import { formatCoord, formatDateTime, prettyEnum } from '../../utils/format'
import { useToast } from '../../components/Toast'
import Loader from '../../components/Loader'
import EmptyState from '../../components/EmptyState'
import StatusBadge from '../../components/StatusBadge'
import MapView from '../../components/MapView'
import MapPicker from '../../components/MapPicker'

export default function TrackingDetail() {
    const { emergencyId } = useParams()
    const toast = useToast()
    const [tracking, setTracking] = useState(null)
    const [emergency, setEmergency] = useState(null)
    const [loading, setLoading] = useState(true)
    const [newStatus, setNewStatus] = useState('EN_ROUTE')
    const [lat, setLat] = useState('')
    const [lng, setLng] = useState('')
    const [saving, setSaving] = useState(false)

    const load = useCallback(async () => {
        const [t, e] = await Promise.allSettled([
            getTrackingByEmergency(emergencyId),
            getEmergencyById(emergencyId),
        ])
        setTracking(t.status === 'fulfilled' ? t.value : null)
        setEmergency(e.status === 'fulfilled' ? e.value : null)
        setLoading(false)
    }, [emergencyId])

    useEffect(() => {
        load()
        const timer = setInterval(load, 4000)
        return () => clearInterval(timer)
    }, [load])

    async function changeStatus() {
        setSaving(true)
        try {
            setTracking(await updateTripStatus(emergencyId, newStatus))
            toast.success('Trip status updated successfully')
        } catch (err) {
            toast.error(getErrorMessage(err))
        } finally {
            setSaving(false)
        }
    }

    async function sendLocation() {
        if (lat === '' || lng === '') {
            toast.error('Click on the map to select a new location')
            return
        }
        setSaving(true)
        try {
            const updated = await updateLocation({
                emergencyId: Number(emergencyId),
                ambulanceId: tracking.ambulanceId,
                latitude: Number(lat),
                longitude: Number(lng),
            })
            setTracking(updated)
            toast.success('Ambulance location updated successfully')
        } catch (err) {
            toast.error(getErrorMessage(err))
        } finally {
            setSaving(false)
        }
    }

    if (loading) return <Loader />
    if (!tracking) {
        return (
            <div>
                <EmptyState message="Tracking has not started yet for this emergency." />
                <Link to="/tracking">Go back</Link>
            </div>
        )
    }

    const markers = [
        {
            lat: tracking.latitude,
            lng: tracking.longitude,
            label: `Ambulance #${tracking.ambulanceId}`,
        },
    ]
    if (emergency) {
        markers.push({
            lat: emergency.latitude,
            lng: emergency.longitude,
            label: `Emergency #${emergency.id}`,
        })
    }

    return (
        <div>
            <div className="page-header">
                <div>
                    <h1>
                        Tracking: Emergency #{emergencyId} <StatusBadge value={tracking.tripStatus} />
                    </h1>
                    <p>
                        Ambulance #{tracking.ambulanceId} | Last update: {formatDateTime(tracking.updatedAt)}
                    </p>
                </div>
                <div className="btn-row">
                    <Link to="/tracking" className="btn btn-secondary">
                        Back
                    </Link>
                    <Link to={`/emergency/${emergencyId}`} className="btn btn-secondary">
                        Emergency details
                    </Link>
                </div>
            </div>

            <div className="card">
                <h2>
                    Live map ({formatCoord(tracking.latitude)}, {formatCoord(tracking.longitude)})
                </h2>
                <MapView markers={markers} zoom={13} />
            </div>

            <div className="grid grid-2">
                <div className="card">
                    <h2>Change trip status</h2>
                    <div className="form-group">
                        <select value={newStatus} onChange={(e) => setNewStatus(e.target.value)}>
                            {TRIP_STATUSES.map((s) => (
                                <option key={s} value={s}>
                                    {prettyEnum(s)}
                                </option>
                            ))}
                        </select>
                    </div>
                    <div className="btn-row" style={{ marginTop: 12 }}>
                        <button className="btn" onClick={changeStatus} disabled={saving}>
                            Update status
                        </button>
                    </div>
                </div>

                <div className="card">
                    <h2>Send ambulance location (demo)</h2>
                    <p className="muted" style={{ fontSize: 13, marginBottom: 10 }}>
                        {lat !== ''
                            ? `Selected: ${formatCoord(lat)}, ${formatCoord(lng)}`
                            : 'Click on the map to select the new ambulance location'}
                    </p>
                    <MapPicker
                        latitude={lat}
                        longitude={lng}
                        onPick={(la, lo) => {
                            setLat(la.toFixed(6))
                            setLng(lo.toFixed(6))
                        }}
                    />
                    <div className="btn-row" style={{ marginTop: 12 }}>
                        <button className="btn" onClick={sendLocation} disabled={saving}>
                            Send location
                        </button>
                    </div>
                </div>
            </div>
        </div>
    )
}