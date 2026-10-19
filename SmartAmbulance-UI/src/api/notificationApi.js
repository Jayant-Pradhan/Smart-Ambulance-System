import client from './client'

const BASE = '/06-notification-service/api/notification'

export const sendNotification = (data) => client.post(BASE, data).then((r) => r.data)

export const getAllNotifications = () => client.get(BASE).then((r) => r.data)

export const getNotificationById = (id) => client.get(`${BASE}/${id}`).then((r) => r.data)

export const getNotificationsByEmergency = (emergencyId) =>
    client.get(`${BASE}/emergency/${emergencyId}`).then((r) => r.data)

export const getNotificationsByStatus = (status) =>
    client.get(`${BASE}/status/${status}`).then((r) => r.data)