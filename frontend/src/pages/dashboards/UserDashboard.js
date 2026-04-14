import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { api } from '../../api/client';
import { useAuth } from '../../context/AuthContext';
import './UserDashboard.css';

export default function UserDashboard() {
  const { user } = useAuth();
  const [stats, setStats] = useState({ bookings: 0, tickets: 0, unread: 0 });
  const [error, setError] = useState('');

  useEffect(() => {
    let cancelled = false;
    (async () => {
      setError('');
      try {
        const [b, t, n] = await Promise.all([
          api.get('/bookings/me'),
          api.get('/tickets?scope=mine'),
          api.get('/notifications/unread-count'),
        ]);
        if (!cancelled) {
          setStats({
            bookings: b.data?.length ?? 0,
            tickets: t.data?.length ?? 0,
            unread: Number(n.data?.count) || 0,
          });
        }
      } catch (e) {
        if (!cancelled) setError(e.message);
      }
    })();
    return () => {
      cancelled = true;
    };
  }, []);

  return (
    <div className="user-dash">
      <section className="user-hero">
        <div>
          <p className="user-kicker">Welcome back</p>
          <h1 className="h1">Staff &amp; student workspace</h1>
          <p className="muted">
            Signed in as <strong>{user?.fullName}</strong> ({user?.email}). Access your resources, bookings, tickets,
            and notifications quickly.
          </p>
        </div>
        <div className="user-chip-wrap">
          <span className="user-chip">Bookings: {stats.bookings}</span>
          <span className="user-chip">Tickets: {stats.tickets}</span>
          <span className="user-chip">Unread: {stats.unread}</span>
        </div>
      </section>
      {error ? <div className="alert error">{error}</div> : null}

      <section className="user-cards">
        <Link className="user-card" to="/bookings">
          <div className="card-title">My bookings</div>
          <div className="card-body muted">{stats.bookings} record(s) in your history.</div>
          <span className="user-link">Open bookings</span>
        </Link>
        <Link className="user-card" to="/tickets">
          <div className="card-title">My tickets</div>
          <div className="card-body muted">{stats.tickets} ticket(s) reported by you.</div>
          <span className="user-link">Open tickets</span>
        </Link>
        <Link className="user-card" to="/notifications">
          <div className="card-title">Notifications</div>
          <div className="card-body muted">{stats.unread} unread message(s).</div>
          <span className="user-link">Open notifications</span>
        </Link>
        <Link className="user-card" to="/resources">
          <div className="card-title">Facilities catalogue</div>
          <div className="card-body muted">Browse rooms, labs, and equipment.</div>
          <span className="user-link">Browse resources</span>
        </Link>
      </section>

      <section className="user-tip panel">
        <div className="callout-title">Quick tips</div>
        <ul className="muted">
          <li>Choose active resources before creating a booking request.</li>
          <li>Add clear descriptions and images when creating maintenance tickets.</li>
          <li>Check notifications often for booking and ticket updates.</li>
        </ul>
      </section>
    </div>
  );
}
