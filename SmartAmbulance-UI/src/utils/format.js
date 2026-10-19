export function formatDateTime(value) {
    if (!value) return '-'
    const date = new Date(value)
    if (isNaN(date.getTime())) return '-'
    return date.toLocaleString('en-IN', {
        day: '2-digit',
        month: 'short',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
    })
}

// "MEDICAL_EMERGENCY" -> "Medical Emergency"
export function prettyEnum(value) {
    if (!value) return '-'
    return value
        .toLowerCase()
        .split('_')
        .map((w) => w.charAt(0).toUpperCase() + w.slice(1))
        .join(' ')
}

export function formatCoord(value) {
    if (value === null || value === undefined) return '-'
    return Number(value).toFixed(5)
}

export function yesNo(value) {
    if (value === null || value === undefined) return '-'
    return value ? 'Yes' : 'No'
}