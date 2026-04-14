import { hasRole } from './roles';

/**
 * Effective dashboard role priority.
 * A user with multiple roles is treated as:
 * ADMIN > TECHNICIAN > USER
 */
export function primaryDashboardRole(user) {
  if (!user?.roles) return 'USER';
  if (hasRole(user, 'ADMIN')) return 'ADMIN';
  if (hasRole(user, 'TECHNICIAN')) return 'TECHNICIAN';
  return 'USER';
}

/**
 * Default landing route after login/register.
 */
export function defaultDashboardPath(user) {
  const role = primaryDashboardRole(user);
  if (role === 'ADMIN') return '/dashboard/admin';
  if (role === 'TECHNICIAN') return '/dashboard/technician';
  return '/dashboard/user';
}
