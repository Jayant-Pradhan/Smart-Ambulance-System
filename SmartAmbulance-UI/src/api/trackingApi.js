import client from './client'

const BASE = '/05-tracking-service/api/tracking'

export const updateLocation = (data) => client.post(`${BASE}/location`, data).then((r) => r.data)

export const getAllTracking = () => client.get(BASE).then((r) => r.data)

export const getTrackingByEmergency = (emergencyId) =>
    client.get(`${BASE}/emergency/${emergencyId}`).then((r) => r.data)

export const getTrackingByAmbulance = (ambulanceId) =>
    client.get(`${BASE}/ambulance/${ambulanceId}`).then((r) => r.data)

export const updateTripStatus = (emergencyId, tripStatus) =>
    client
        .patch(`${BASE}/emergency/${emergencyId}/status`, null, { params: { tripStatus } })
        .then((r) => r.data)