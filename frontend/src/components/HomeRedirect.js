import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { defaultDashboardPath } from '../utils/dashboardPath';

export default function HomeRedirect() {
  const { user } = useAuth();
  return <Navigate to={defaultDashboardPath(user)} replace />;
}
