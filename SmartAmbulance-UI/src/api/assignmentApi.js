import client from './client'

const BASE = '/03-ambulance-assignment-service/api/assignments'

export const getAllAssignments = () => client.get(BASE).then((r) => r.data)

export const getAssignmentById = (id) => client.get(`${BASE}/${id}`).then((r) => r.data)

export const getAssignmentByEmergencyId = (emergencyId) =>
    client.get(`${BASE}/emergency/${emergencyId}`).then((r) => r.data)

export const getAssignmentsByStatus = (status) =>
    client.get(`${BASE}/status/${status}`).then((r) => r.data)

export const updateAssignmentStatus = (id, status) =>
    client.patch(`${BASE}/${id}/status`, { status }).then((r) => r.data)

// Normally assignment auto hoti hai (Kafka se). Ye sirf manual retry ke liye hai.
export const assignAmbulance = (emergencyId) =>
    client.post(`${BASE}/emergency/${emergencyId}`).then((r) => r.data)