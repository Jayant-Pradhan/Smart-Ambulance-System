import client from './client'

const BASE = '/01-emergency-service/api/emergency'

export const createEmergency = (data) => client.post(BASE, data).then((r) => r.data)

export const getAllEmergencies = () => client.get(BASE).then((r) => r.data)

export const getEmergencyById = (id) => client.get(`${BASE}/${id}`).then((r) => r.data)

export const getEmergencyStatus = (id) => client.get(`${BASE}/${id}/status`).then((r) => r.data)

export const cancelEmergency = (id) => client.patch(`${BASE}/${id}/cancel`).then((r) => r.data)