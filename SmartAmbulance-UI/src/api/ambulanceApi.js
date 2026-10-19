import client from './client'

const BASE = '/02-ambulance-service/api/ambulance'

export const registerAmbulance = (data) => client.post(BASE, data).then((r) => r.data)

export const getAllAmbulances = () => client.get(BASE).then((r) => r.data)

export const getAmbulanceById = (id) => client.get(`${BASE}/${id}`).then((r) => r.data)

export const updateAmbulanceStatus = (id, status) =>
    client.patch(`${BASE}/${id}/status`, { status }).then((r) => r.data)

export const updateAmbulanceLocation = (id, latitude, longitude) =>
    client.patch(`${BASE}/${id}/location`, { latitude, longitude }).then((r) => r.data)