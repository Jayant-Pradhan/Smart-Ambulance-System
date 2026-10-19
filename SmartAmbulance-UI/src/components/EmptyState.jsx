export default function EmptyState({ message = 'Koi data nahi mila.' }) {
    return <div className="empty">{message}</div>
}