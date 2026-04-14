import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { api } from '../api/client';
import { useAuth } from '../context/AuthContext';
import { hasRole } from '../utils/roles';
import './OperationsPages.css';

function ticketStatusClass(s) {
  if (s === 'RESOLVED' || s === 'CLOSED') return 'ok';
  if (s === 'REJECTED') return 'bad';
  return 'neutral';
}

export default function TicketsPage() {
  const { user } = useAuth();
  const tech = hasRole(user, 'TECHNICIAN');
  const admin = hasRole(user, 'ADMIN');
  const [scope, setScope] = useState(admin ? 'all' : tech ? 'assigned' : 'mine');
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const load = async () => {
    setLoading(true);
    setError('');
    try {
      const { data } = await api.get(`/tickets?scope=${encodeURIComponent(scope)}`);
      setItems(data);
    } catch (e) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [scope, admin]);

  const activeCount = items.filter((t) => t.status === 'OPEN' || t.status === 'IN_PROGRESS').length;
  const resolvedCount = items.filter((t) => t.status === 'RESOLVED' || t.status === 'CLOSED').length;

  return (
    <div className="ops-page tickets-page">
      <div className="ops-hero row space-between wrap gap">
        <div>
          <h1 className="h1">Maintenance and incidents</h1>
          <p className="muted">
            {tech
              ? 'View and work only your assigned tickets.'
              : 'Create tickets, attach up to three images, and collaborate with staff comments.'}
          </p>
        </div>
        {!tech ? (
          <Link className="btn primary" to="/tickets/new">
            New ticket
          </Link>
        ) : null}
      </div>
      <div className="ops-chip-row">
        <span className="ops-chip">Total: {items.length}</span>
        <span className="ops-chip">Active: {activeCount}</span>
        <span className="ops-chip">Resolved/Closed: {resolvedCount}</span>
      </div>

      {admin ? (
        <div className="row gap ops-scope-row" style={{ marginTop: 12 }}>
          <button type="button" className={`btn ${scope === 'all' ? 'primary' : 'ghost'}`} onClick={() => setScope('all')}>
            All tickets
          </button>
          <button type="button" className={`btn ${scope === 'mine' ? 'primary' : 'ghost'}`} onClick={() => setScope('mine')}>
            My tickets
          </button>
          <button type="button" className={`btn ${scope === 'assigned' ? 'primary' : 'ghost'}`} onClick={() => setScope('assigned')}>
            Assigned to me
          </button>
        </div>
      ) : null}

      {error ? <div className="alert error">{error}</div> : null}
      {loading ? <div className="muted">Loading…</div> : null}

      <div className="table-wrap ops-table-wrap" style={{ marginTop: 14 }}>
        <table className="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Category</th>
              <th>Priority</th>
              <th>Status</th>
              <th>Updated</th>
              <th />
            </tr>
          </thead>
          <tbody>
            {items.map((t) => (
              <tr key={t.id}>
                <td>#{t.id}</td>
                <td>{t.category}</td>
                <td>
                  <span className="tag">{t.priority}</span>
                </td>
                <td>
                  <span className={`status ${ticketStatusClass(t.status)}`}>{t.status}</span>
                </td>
                <td className="muted">{new Date(t.updatedAt).toLocaleString()}</td>
                <td className="right">
                  <Link className="btn small ghost" to={`/tickets/${t.id}`}>
                    Open
                  </Link>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
