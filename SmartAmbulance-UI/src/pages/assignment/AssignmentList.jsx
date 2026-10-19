import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { getAllAssignments, updateAssignmentStatus } from '../../api/assignmentApi'
import { getErrorMessage } from '../../utils/errors'
import { ASSIGNMENT_STATUSES } from '../../utils/constants'
import { formatDateTime, prettyEnum } from '../../utils/format'
import { useToast } from '../../components/Toast'
import Loader from '../../components/Loader'
import EmptyState from '../../components/EmptyState'
import StatusBadge from '../../components/StatusBadge'
import Modal from '../../components/Modal'

export default function AssignmentList() {
    const toast = useToast()
    const [assignments, setAssignments] = useState([])
    const [loading, setLoading] = useState(true)
    const [status, setStatus] = useState('')
    const [target, setTarget] = useState(null)
    const [newStatus, setNewStatus] = useState('ACCEPTED')
    const [saving, setSaving] = useState(false)

    async function load() {
        setLoading(true)
        try {
            setAssignments(await getAllAssignments())
        } catch (err) {
            toast.error(getErrorMessage(err))
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        load()
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])

    function openModal(a) {
        setTarget(a)
        setNewStatus(a.assignmentStatus || 'ACCEPTED')
    }

    async function save() {
        setSaving(true)
        try {
            const updated = await updateAssignmentStatus(target.assignmentId, newStatus)
            setAssignments((list) =>
                list.map((a) => (a.assignmentId === updated.assignmentId ? updated : a)),
            )
            toast.success('Assignment status updated')
            setTarget(null)
        } catch (err) {
            toast.error(getErrorMessage(err))
        } finally {
            setSaving(false)
        }
    }

    const filtered = assignments
        .filter((a) => !status || a.assignmentStatus === status)
        .sort((a, b) => b.assignmentId - a.assignmentId)

    return (
        <div>
            <div className="page-header">
                <div>
                    <h1>Assignments</h1>
                    <p>An ambulance is automatically assigned to an emergency, its status is displayed here</p>
                </div>
                <button className="btn btn-secondary" onClick={load}>
                    Refresh
                </button>
            </div>

            <div className="card">
                <div className="filters">
                    <select value={status} onChange={(e) => setStatus(e.target.value)}>
                        <option value="">All statuses</option>
                        {ASSIGNMENT_STATUSES.map((s) => (
                            <option key={s} value={s}>
                                {prettyEnum(s)}
                            </option>
                        ))}
                    </select>
                </div>

                {loading ? (
                    <Loader />
                ) : filtered.length === 0 ? (
                    <EmptyState message="No assignments found."/>
                ) : (
                    <div className="table-wrap">
                        <table>
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Emergency</th>
                                <th>Ambulance</th>
                                <th>Distance</th>
                                <th>Status</th>
                                <th>Assigned at</th>
                                <th>Completed at</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            {filtered.map((a) => (
                                <tr key={a.assignmentId}>
                                    <td>#{a.assignmentId}</td>
                                    <td>
                                        <Link to={`/emergency/${a.emergencyId}`}>#{a.emergencyId}</Link>
                                    </td>
                                    <td>#{a.ambulanceId}</td>
                                    <td>
                                        {a.distanceInKm != null ? `${Number(a.distanceInKm).toFixed(2)} km` : '-'}
                                    </td>
                                    <td>
                                        <StatusBadge value={a.assignmentStatus} />
                                    </td>
                                    <td>{formatDateTime(a.assignedAt)}</td>
                                    <td>{formatDateTime(a.completedAt)}</td>
                                    <td>
                                        <button className="btn btn-secondary btn-sm" onClick={() => openModal(a)}>
                                            Update status
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>

            {target && (
                <Modal title={`Assignment #${target.assignmentId}`} onClose={() => setTarget(null)}>
                    <div className="form-group">
                        <label>New status</label>
                        <select value={newStatus} onChange={(e) => setNewStatus(e.target.value)}>
                            {ASSIGNMENT_STATUSES.map((s) => (
                                <option key={s} value={s}>
                                    {prettyEnum(s)}
                                </option>
                            ))}
                        </select>
                    </div>
                    <div className="btn-row" style={{ marginTop: 16 }}>
                        <button className="btn" onClick={save} disabled={saving}>
                            {saving ? 'Saving...' : 'Save'}
                        </button>
                        <button className="btn btn-secondary" onClick={() => setTarget(null)}>
                            Cancel
                        </button>
                    </div>
                </Modal>
            )}
        </div>
    )
}