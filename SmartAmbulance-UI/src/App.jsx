import { Routes, Route } from 'react-router-dom'
import { ToastProvider } from './components/Toast'
import Layout from './components/Layout'

import Dashboard from './pages/Dashboard'
import EmergencyList from './pages/emergency/EmergencyList'
import EmergencyCreate from './pages/emergency/EmergencyCreate'
import EmergencyDetail from './pages/emergency/EmergencyDetail'
import AmbulanceList from './pages/ambulance/AmbulanceList'
import AmbulanceRegister from './pages/ambulance/AmbulanceRegister'
import AssignmentList from './pages/assignment/AssignmentList'
import HospitalList from './pages/hospital/HospitalList'
import HospitalDetail from './pages/hospital/HospitalDetail'
import TrackingList from './pages/tracking/TrackingList'
import TrackingDetail from './pages/tracking/TrackingDetail'
import NotificationList from './pages/notification/NotificationList'

export default function App() {
    return (
        <ToastProvider>
            <Routes>
                <Route element={<Layout />}>
                    <Route path="/" element={<Dashboard />} />

                    <Route path="/emergency" element={<EmergencyList />} />
                    <Route path="/emergency/new" element={<EmergencyCreate />} />
                    <Route path="/emergency/:id" element={<EmergencyDetail />} />

                    <Route path="/ambulance" element={<AmbulanceList />} />
                    <Route path="/ambulance/new" element={<AmbulanceRegister />} />

                    <Route path="/assignment" element={<AssignmentList />} />

                    <Route path="/hospital" element={<HospitalList />} />
                    <Route path="/hospital/:id" element={<HospitalDetail />} />

                    <Route path="/tracking" element={<TrackingList />} />
                    <Route path="/tracking/:emergencyId" element={<TrackingDetail />} />

                    <Route path="/notification" element={<NotificationList />} />
                </Route>
            </Routes>
        </ToastProvider>
    )
}