import client from './client'

const HOSPITALS = '/04-hospital-service/api/hospitals'
const DEPARTMENTS = '/04-hospital-service/api/departments'
const BEDS = '/04-hospital-service/api/beds'

// ---------- Hospitals ----------
export const createHospital = (data) => client.post(HOSPITALS, data).then((r) => r.data)
export const getAllHospitals = () => client.get(HOSPITALS).then((r) => r.data)
export const getHospitalById = (id) => client.get(`${HOSPITALS}/${id}`).then((r) => r.data)
export const updateHospital = (id, data) =>
    client.patch(`${HOSPITALS}/${id}`, data).then((r) => r.data)
export const deactivateHospital = (id) =>
    client.patch(`${HOSPITALS}/${id}/deactivate`).then((r) => r.data)

// ---------- Departments ----------
export const createDepartment = (data) => client.post(DEPARTMENTS, data).then((r) => r.data)
export const getAllDepartments = () => client.get(DEPARTMENTS).then((r) => r.data)
export const getDepartmentsByHospital = (hospitalId) =>
    client.get(`${DEPARTMENTS}/hospital/${hospitalId}`).then((r) => r.data)
export const updateDepartment = (id, data) =>
    client.patch(`${DEPARTMENTS}/${id}`, data).then((r) => r.data)
export const deactivateDepartment = (id) =>
    client.patch(`${DEPARTMENTS}/${id}/deactivate`).then((r) => r.data)

// ---------- Beds ----------
export const createBed = (data) => client.post(BEDS, data).then((r) => r.data)
export const getAllBeds = () => client.get(BEDS).then((r) => r.data)
export const getBedsByHospital = (hospitalId) =>
    client.get(`${BEDS}/hospital/${hospitalId}`).then((r) => r.data)
export const getAvailableBeds = (hospitalId, bedType) =>
    client.get(`${BEDS}/hospital/${hospitalId}/available`, { params: { bedType } }).then((r) => r.data)
export const updateBed = (id, data) => client.patch(`${BEDS}/${id}`, data).then((r) => r.data)
export const reserveBed = (id) => client.patch(`${BEDS}/${id}/reserve`).then((r) => r.data)
export const occupyBed = (id) => client.patch(`${BEDS}/${id}/occupy`).then((r) => r.data)
export const releaseBed = (id) => client.patch(`${BEDS}/${id}/release`).then((r) => r.data)
export const markBedOutOfService = (id) =>
    client.patch(`${BEDS}/${id}/out-of-service`).then((r) => r.data)
export const markBedAvailable = (id) =>
    client.patch(`${BEDS}/${id}/available`).then((r) => r.data)
export const deactivateBed = (id) => client.patch(`${BEDS}/${id}/deactivate`).then((r) => r.data)