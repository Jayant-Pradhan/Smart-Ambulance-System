import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet'
import L from 'leaflet'
import markerIcon from 'leaflet/dist/images/marker-icon.png'
import markerIcon2x from 'leaflet/dist/images/marker-icon-2x.png'
import markerShadow from 'leaflet/dist/images/marker-shadow.png'
import { DEFAULT_CENTER } from '../utils/constants'

delete L.Icon.Default.prototype._getIconUrl
L.Icon.Default.mergeOptions({
    iconUrl: markerIcon,
    iconRetinaUrl: markerIcon2x,
    shadowUrl: markerShadow,
})

// markers: [{ lat, lng, label }]
export default function MapView({ markers = [], zoom = 12 }) {
    const valid = markers.filter(
        (m) => m.lat !== null && m.lat !== undefined && m.lng !== null && m.lng !== undefined,
    )
    const center = valid.length > 0 ? [Number(valid[0].lat), Number(valid[0].lng)] : DEFAULT_CENTER

    return (
        <div className="map-box">
            <MapContainer
                key={center.join(',')}
                center={center}
                zoom={zoom}
                style={{ height: '100%', width: '100%' }}
            >
                <TileLayer
                    attribution="&copy; OpenStreetMap contributors"
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />
                {valid.map((m, i) => (
                    <Marker key={i} position={[Number(m.lat), Number(m.lng)]}>
                        {m.label && <Popup>{m.label}</Popup>}
                    </Marker>
                ))}
            </MapContainer>
        </div>
    )
}