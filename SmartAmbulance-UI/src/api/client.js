import axios from 'axios'

const client = axios.create({
    baseURL: 'http://localhost:8087',
    headers: { 'Content-Type': 'application/json' },
    timeout: 15000,
})

export default client