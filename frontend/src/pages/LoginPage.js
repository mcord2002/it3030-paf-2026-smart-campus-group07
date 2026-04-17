import React, { useState } from 'react';
import { Link, Navigate, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { defaultDashboardPath } from '../utils/dashboardPath';
import './AuthPages.css';

export default function LoginPage() {
  const { login, isAuthenticated, user } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  if (isAuthenticated) {
    const from = location.state?.from?.pathname;
    const to =
      from && from !== '/login' && !from.startsWith('/register') && !from.startsWith('/dashboard')
        ? from
        : defaultDashboardPath(user);
    return <Navigate to={to} replace />;
  }
//validation
  const onSubmit = async (e) => {
    e.preventDefault();
    setError('');
    const cleanEmail = email.trim().toLowerCase();
    const cleanPassword = password.trim();
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(cleanEmail)) {
      setError('Please enter a valid email address.');
      return;
    }
    if (!cleanPassword) {
      setError('Password is required.');
      return;
    }
    setLoading(true);
    try {
      const nextUser = await login(cleanEmail, cleanPassword);
      const from = location.state?.from?.pathname;
      if (from && from !== '/login' && !from.startsWith('/register') && !from.startsWith('/dashboard')) {
        navigate(from, { replace: true });
      } else {
        navigate(defaultDashboardPath(nextUser), { replace: true });
      }
    } catch (err) {
      setError(err.message || 'Login failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-shell login-shell">
      <div className="auth-card">
        <div className="auth-brand">Campus Hub</div>
        <div className="auth-badge">Secure access portal</div>
        <h1 className="auth-h1">Sign in</h1>
        <p className="muted">Use your university email.</p>
        <form className="form" onSubmit={onSubmit}>
          <label className="label">
            Email
            <input className="input" value={email} onChange={(e) => setEmail(e.target.value)} type="email" required maxLength={120} />
          </label>
          <label className="label">
            Password
            <input
              className="input"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              type="password"
              required
            />
          </label>
          {error ? <div className="alert error">{error}</div> : null}
          <button className="btn primary full" type="submit" disabled={loading}>
            {loading ? 'Signing in…' : 'Sign in'}
          </button>
        </form>
        <div className="auth-footer muted">
          No account? <Link to="/register">Create one</Link>
        </div>
        <div className="hint muted">
          Demo accounts (password <code>ChangeMe123!</code>):<br />
          <code>admin@campus.edu</code>, <code>tech@campus.edu</code>, <code>user@campus.edu</code>
        </div>
      </div>
    </div>
  );
}
