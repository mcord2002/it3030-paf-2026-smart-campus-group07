import React, { useEffect, useMemo, useState } from 'react';
import { Link } from 'react-router-dom';
import { api } from '../../api/client';
import { useAuth } from '../../context/AuthContext';
import './TechnicianDashboard.css';

export default function TechnicianDashboard() {
  const { user } = useAuth();
  const [assigned, setAssigned] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    let cancelled = false;
    (async () => {
      setError('');
      try {
        const { data } = await api.get('/tickets?scope=assigned');
        if (!cancelled) setAssigned(data || []);
      } catch (e) {
        if (!cancelled) setError(e.message);
      }
    })();
    return () => {
      cancelled = true;
    };
  }, []);

  const actionable = useMemo(
    () => assigned.filter((t) => t.status === 'OPEN' || t.status === 'IN_PROGRESS'),
    [assigned]
  );
  const resolved = useMemo(() => assigned.filter((t) => t.status === 'RESOLVED').length, [assigned]);

  return (
    <div className="tech-dash">
      <section className="tech-hero">
        <div>
          <p className="tech-kicker">Technician console</p>
          <h1 className="h1">Technician workspace</h1>
          <p className="muted">
            Signed in as <strong>{user?.fullName}</strong>. Work your assigned queue, update ticket status, and add
            resolution notes.
          </p>
        </div>
        <div className="tech-chip-wrap">
          <span className="tech-chip">Assigned: {assigned.length}</span>
          <span className="tech-chip">Need action: {actionable.length}</span>
          <span className="tech-chip">Resolved: {resolved}</span>
        </div>
      </section>
      {error ? <div className="alert error">{error}</div> : null}
      <section className="tech-cards">
        <Link className="tech-card" to="/tickets">
          <div className="card-title">Assigned tickets</div>
          <div className="card-body muted">
            <strong>{assigned.length}</strong> total assigned · <strong>{actionable.length}</strong> need progress.
          </div>
          <span className="tech-link">Open ticket board</span>
        </Link>
        <Link className="tech-card" to="/notifications">
          <div className="card-title">Notifications</div>
          <div className="card-body muted">Assignments and ticket updates appear here.</div>
          <span className="tech-link">Open notifications</span>
        </Link>
      </section>
      {assigned.length > 0 ? (
        <div className="panel tech-queue-panel" style={{ marginTop: 2 }}>
          <div style={{ fontWeight: 700, marginBottom: 10 }}>Quick queue</div>
          <div className="table-wrap tech-table-wrap">
            <table className="table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Category</th>
                  <th>Status</th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {assigned.slice(0, 8).map((t) => (
                  <tr key={t.id}>
                    <td>#{t.id}</td>
                    <td>{t.category}</td>
                    <td>{t.status}</td>
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
      ) : null}
    </div>
  );
}
