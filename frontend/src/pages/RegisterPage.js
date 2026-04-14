import React, { useState } from 'react';
import { Link, Navigate, useNavigate, useParams } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { defaultDashboardPath } from '../utils/dashboardPath';
import './AuthPages.css';

export default function RegisterPage() {
  const { register, isAuthenticated, user } = useAuth();
  const navigate = useNavigate();
  const { roleType } = useParams();
  const normalizedRole = roleType === 'admin' ? 'ADMIN' : roleType === 'technician' ? 'TECHNICIAN' : 'USER';
  const [form, setForm] = useState({
    email: '',
    password: '',
    fullName: '',
    phone: '',
    accountType: normalizedRole,
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  if (isAuthenticated) return <Navigate to={defaultDashboardPath(user)} replace />;

  const onChange = (k) => (e) => setForm((f) => ({ ...f, [k]: e.target.value }));

  const onSubmit = async (e) => {
    e.preventDefault();
    setError('');
    const fullName = form.fullName.trim();
    const email = form.email.trim().toLowerCase();
    const password = form.password.trim();
    const phone = form.phone.trim();
    if (fullName.length < 3) {
      setError('Full name must be at least 3 characters.');
      return;
    }
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      setError('Please enter a valid email address.');
      return;
    }
    if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/.test(password)) {
      setError('Password must have 8+ chars, including uppercase, lowercase, and a number.');
      return;
    }
    if (phone && !/^[+]?[0-9()\-\s]{7,20}$/.test(phone)) {
      setError('Phone number format is invalid.');
      return;
    }
    setLoading(true);
    try {
      const nextUser = await register({
        email,
        password,
        fullName,
        phone: phone || undefined,
        accountType: normalizedRole,
      });
      navigate(defaultDashboardPath(nextUser), { replace: true });
    } catch (err) {
      setError(err.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-shell register-shell">
      <div className="auth-card">
        <div className="auth-brand">Campus Hub</div>
        <div className="auth-badge">Role-based onboarding</div>
        <h1 className="auth-h1">
          {normalizedRole === 'ADMIN'
            ? 'Create admin account'
            : normalizedRole === 'TECHNICIAN'
              ? 'Create technician account'
              : 'Create user account'}
        </h1>
        <p className="muted">
          {normalizedRole === 'ADMIN'
            ? 'Register as an admin account.'
            : normalizedRole === 'TECHNICIAN'
              ? 'Register as a technician account.'
              : 'Register as a user account. You will be signed in after registration.'}
        </p>
        <div className="auth-role-switch">
          <Link className={`btn ${normalizedRole === 'USER' ? 'primary' : 'ghost'}`} to="/register/user">
            User register
          </Link>
          <Link className={`btn ${normalizedRole === 'TECHNICIAN' ? 'primary' : 'ghost'}`} to="/register/technician">
            Technician register
          </Link>
          <Link className={`btn ${normalizedRole === 'ADMIN' ? 'primary' : 'ghost'}`} to="/register/admin">
            Admin register
          </Link>
        </div>
        <form className="form" onSubmit={onSubmit}>
          <label className="label">
            Full name
            <input className="input" value={form.fullName} onChange={onChange('fullName')} minLength={3} maxLength={120} required />
          </label>
          <label className="label">
            Email
            <input className="input" value={form.email} onChange={onChange('email')} type="email" maxLength={120} required />
          </label>
          <label className="label">
            Phone (optional)
            <input className="input" value={form.phone} onChange={onChange('phone')} pattern="^[+]?[0-9()\-\s]{7,20}$" maxLength={20} />
          </label>
          <label className="label">
            Password (min 8)
            <input className="input" value={form.password} onChange={onChange('password')} type="password" minLength={8} maxLength={100} required />
          </label>
          <input type="hidden" value={normalizedRole} readOnly />
          {error ? <div className="alert error">{error}</div> : null}
          <button className="btn primary full" type="submit" disabled={loading}>
            {loading ? 'Creating…' : `Create ${normalizedRole.toLowerCase()} account`}
          </button>
        </form>
        <div className="auth-footer muted">
          Already have an account? <Link to="/login">Sign in</Link>
        </div>
      </div>
    </div>
  );
}
