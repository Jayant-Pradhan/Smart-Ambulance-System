export function getErrorMessage(error) {
    if (!error.response) {
        return 'Could not connect to the server. Please check if the backend and gateway are running.'
    }

    const data = error.response.data

    if (!data) return `Error ${error.response.status}`

    // Plain text (Assignment service)
    if (typeof data === 'string') return data

    // { message: "..." }  (Emergency, Hospital, Tracking, Notification)
    if (data.message && typeof data.message === 'string') return data.message

    // { error: "..." }  (Ambulance)
    if (data.error && typeof data.error === 'string') return data.error

    // Validation errors: { fieldName: "message", ... }
    if (typeof data === 'object') {
        const values = Object.values(data).filter((v) => typeof v === 'string')
        if (values.length > 0) return values.join(', ')
    }

    return `Error ${error.response.status}`
}