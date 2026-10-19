import { MapContainer, TileLayer, Marker, useMapEvents } from 'react-leaflet'
import L from 'leaflet'
import markerIcon from 'leaflet/dist/images/marker-icon.png'
import markerIcon2x from 'leaflet/dist/images/marker-icon-2x.png'
import markerShadow from 'leaflet/dist/images/marker-shadow.png'
import { DEFAULT_CENTER } from '../utils/constants'

// Leaflet ke default marker icon ka Vite fix
delete L.Icon.Default.prototype._getIconUrl
L.Icon.Default.mergeOptions({
    iconUrl: markerIcon,
    iconRetinaUrl: markerIcon2x,
    shadowUrl: markerShadow,
})

function ClickHandler({ onPick }) {
    useMapEvents({
        click(e) {
            onPick(e.latlng.lat, e.latlng.lng)
        },
    })
    return null
}

export default function MapPicker({ latitude, longitude, onPick }) {
    const hasPoint = latitude !== '' && longitude !== '' && latitude != null && longitude != null
    const center = hasPoint ? [Number(latitude), Number(longitude)] : DEFAULT_CENTER

    return (
        <div className="map-box">
            <MapContainer center={center} zoom={12} style={{ height: '100%', width: '100%' }}>
                <TileLayer
                    attribution="&copy; OpenStreetMap contributors"
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />
                <ClickHandler onPick={onPick} />
                {hasPoint && <Marker position={[Number(latitude), Number(longitude)]} />}
            </MapContainer>
        </div>
    )
}