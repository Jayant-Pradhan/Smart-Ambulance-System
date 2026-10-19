import { NavLink } from 'react-router-dom'

const links = [
    { to: '/', label: 'Dashboard', end: true },
    { to: '/emergency', label: 'Emergencies' },
    { to: '/ambulance', label: 'Ambulances' },
    { to: '/assignment', label: 'Assignments' },
    { to: '/hospital', label: 'Hospitals' },
    { to: '/tracking', label: 'Tracking' },
    { to: '/notification', label: 'Notifications' },
]

export default function Sidebar() {
    return (
        <aside className="sidebar">
            <div className="brand">
                Smart <span>Ambulance</span>
            </div>
            <nav>
                {links.map((l) => (
                    <NavLink
                        key={l.to}
                        to={l.to}
                        end={l.end}
                        className={({ isActive }) => (isActive ? 'active' : '')}
                    >
                        {l.label}
                    </NavLink>
                ))}
            </nav>
        </aside>
    )
}