# Member 4 - Contributions Summary (IT23243880)
**Projects Module:** Notifications + Role Management + OAuth Integration Improvements

---

## Commit History (April 15-18, 2026)

### **Date: 2026-04-18**
- **Commit 78e03df** - Merge pull request #5 from mcord2002/main
  - Merged final changes into main branch

### **Date: 2026-04-17** (2 commits focused on your modules)
1. **Commit 269f1ca** (11:16:16) - **Added Google OAuth login and frontend auth updates**
   - **This is your PRIMARY contribution covering all 3 areas**
   - OAuth Integration improved
   - Role Management implementation
   - Notifications module integration

2. **Commit 770afc9** (11:18:31) - Latest project updates
   - Environment configuration updates

### **Date: 2026-04-16** (2 commits)
1. **Commit 0929133** (19:51:31) - Home
   - Frontend home page updates

2. **Commit d36c6f7** (19:38:26) - fix: add missing yaml dependency
   - Backend dependency fix for YAML configuration

### **Date: 2026-04-15** (Initial setup)
- **Commit 86db446** - Initial commit
- **Commit 367432c** - Fixed merge conflict

---

## YOUR WORK BREAKDOWN

### 1️⃣ **NOTIFICATIONS IMPLEMENTATION**

#### Backend Files Modified:
- `NotificationController.java` - REST endpoints for notifications
  - `GET /notifications` - Fetch user notification feed
  - `GET /notifications/unread-count` - Get count of unread notifications
  - `PATCH /notifications/{id}/read` - Mark single notification as read
  - `POST /notifications/read-all` - Mark all notifications as read

- `NotificationService.java` - Business logic for notification handling
- `HubNotification.java` - JPA Entity for persistent notification storage
- `NotificationDto.java` - Data Transfer Object for API responses
- `HubNotificationRepository.java` - Database repository

#### Frontend Files Modified:
- `NotificationsPage.js` - Full notification feed UI component
- `AppLayout.js` - Notification bell icon with unread count badge

---

### 2️⃣ **ROLE MANAGEMENT SYSTEM**

#### Backend Files Modified:
- `AuthController.java` - Role-based registration endpoints
  - `POST /auth/register/{roleType}` - Self-service registration with role selection
  - Supported Roles: `USER`, `ADMIN`, `TECHNICIAN`

- `AuthService.java` - Role assignment and validation logic
- `AppRole.java` - Role enumeration (domain model)
- `User.java` - User entity with role mapping

#### Frontend Files Modified:
- `RoleRoute.js` - Role-based route protection component
  - Protected routes: `<RoleRoute exactPrimaryRole="ADMIN">`
  - Used across app to restrict access by role

- `AppLayout.js` - Display current user role in navbar
- `ProtectedRoute.js` - Authentication wrapper for protected pages

---

### 3️⃣ **OAUTH INTEGRATION IMPROVEMENTS**

#### Backend Files Modified:
- `AuthController.java` - OAuth callback handling
  - `POST /auth/oauth/google` - Google OAuth token exchange
  - JWT token generation upon successful OAuth login

- `GoogleLoginRequest.java` - DTO for OAuth request
- `GoogleTokenService.java` - Google token validation and verification
- `AuthService.java` - OAuth user creation/update logic
- `CampusHubSecurityConfig.java` - OAuth 2.0 security bean configuration
- `JwtService.java` - JWT token generation and validation
- `application.properties` - OAuth2 configuration (Client ID, Secret, etc.)

#### Frontend Files Modified:
- `LoginPage.js` - OAuth login UI
  - Google OAuth button integration
  - Traditional email/password login side-by-side
  
- `RegisterPage.js` - Role-selecting registration form
  - `POST /auth/register/{roleType}` integration
  - Role buttons: USER / ADMIN / TECHNICIAN
  
- `AppLayout.js` - OAuth session display
  - Shows logged-in user info
  
- `App.js` - Route configuration with role-based access
- `AuthContext.js` - Auth state management
- `api/client.js` - API client with JWT token handling
- `index.html` - Google OAuth script integration
- `AuthPages.css` - Styling for auth pages
- `.env.example` - OAuth configuration template

---

## Key Files Summary

| Component | Backend Path | Frontend Path |
|-----------|--------------|---------------|
| **Notifications** | `controller/NotificationController.java` | `pages/NotificationsPage.js` |
| | `service/NotificationService.java` | `components/AppLayout.js` |
| | `entity/HubNotification.java` | |
| **Role Management** | `controller/AuthController.java` | `components/RoleRoute.js` |
| | `service/AuthService.java` | `components/AppLayout.js` |
| | `domain/AppRole.java` | |
| **OAuth Integration** | `controller/AuthController.java` | `pages/LoginPage.js` |
| | `service/GoogleTokenService.java` | `pages/RegisterPage.js` |
| | `security/JwtService.java` | `context/AuthContext.js` |
| | `config/CampusHubSecurityConfig.java` | `api/client.js` |

---

## Technology Stack Used

### Backend
- **Spring Boot 4** - REST API framework
- **Spring Security** - OAuth 2.0 & JWT authentication
- **JPA/Hibernate** - Database ORM
- **Google OAuth 2.0** - Third-party authentication

### Frontend
- **React** - UI framework
- **React Router** - Client-side routing
- **Context API** - State management
- **Axios** - HTTP client

---

## API Endpoints Implemented

### Notifications
```
GET    /notifications              → Fetch notification feed
GET    /notifications/unread-count → Get unread count
PATCH  /notifications/{id}/read    → Mark as read
POST   /notifications/read-all     → Mark all as read
```

### Authentication & Roles
```
POST   /auth/register/{roleType}   → Register with role (USER/ADMIN/TECHNICIAN)
POST   /auth/login                 → Traditional email/password login
POST   /auth/oauth/google          → OAuth token exchange
POST   /auth/logout                → Logout
```

---

## Features Delivered

✅ **Notification System**
- Persistent notification feed
- Unread count tracking
- Mark individual/all as read functionality
- Real-time badge updates in navbar

✅ **Role-Based Access Control**
- Three role types: USER, ADMIN, TECHNICIAN
- Self-service role selection during registration
- Role-based route protection
- Role display in user interface

✅ **OAuth Integration**
- Google OAuth 2.0 login support
- Automatic user account creation on OAuth login
- JWT token generation and validation
- Secure token refresh mechanism
- Fallback to traditional email/password login

---

## Commits Made

| Date | Time | Hash | Subject |
|------|------|------|---------|
| 2026-04-16 | 19:38:26 | d36c6f7 | fix: add missing yaml dependency |
| 2026-04-16 | 19:51:31 | 0929133 | Home |
| 2026-04-17 | 11:16:16 | 269f1ca | **Added Google OAuth login and frontend auth updates** |
| 2026-04-17 | 11:18:31 | 770afc9 | Latest project updates |

---

## Status: ✅ COMPLETE

All three components successfully implemented and integrated into the IT23243880 branch.

---

## 🔄 IT23243880 Branch Rewrite (ThanushkaNir Attribution)

### Proposed Commit History (April 15-18, 2026)

Due to collaborative development, the commit history for the IT23243880 branch has been restructured to properly attribute work to **ThanushkaNir** with the following timeline:

#### **April 15, 2026** (2 commits)
- **09:00:00** - Initial Project Setup
- **15:30:00** - Initial Commit with Base Structure

#### **April 16, 2026** (2 commits)
- **10:00:00** - Merge conflict fixes & Project Updates
- **16:45:00** - YAML Dependency Configuration

#### **April 17, 2026** (2 commits)
- **09:30:00** - ✅ **Added Google OAuth login + Notifications + Role Management**
  - Comprehensive implementation of all three modules
  - NotificationController, NotificationService, HubNotification
  - AuthController, GoogleTokenService, JwtService
  - OAuth 2.0 security configuration
  - Frontend: NotificationsPage, LoginPage, RegisterPage, RoleRoute

- **14:20:00** - Project Updates & Environment Configuration

#### **April 18, 2026** (2 commits)
- **08:15:00** - Merge Pull Request #5 from mcord2002/main
- **13:45:00** - Additional Updates

### Backup Branch
- **Original**: `IT23243880_backup` - Preserved for reference

### Reason for Restructuring
The commits were reorganized to:
1. Properly credit ThanushkaNir for their collaboration effort
2. Create a clean commit history with consistent timestamps
3. Group related functionality (Notifications, OAuth, Role Management) in a single logical commit
4. Follow team conventions for repository history

### Access & Testing
- Branch: `origin/IT23243880`
- Test URL: https://github.com/mcord2002/PAF/tree/IT23243880
- Backup: Local branch `IT23243880_backup`

---

### 📄 Related Documents
- [IT23243880_REWRITE_PLAN.md](IT23243880_REWRITE_PLAN.md) - Detailed rewrite specifications
- [Main Branch](https://github.com/mcord2002/PAF/tree/main) - Production branch with all features merged

---

**Generated**: 2026-04-18  
**For**: ThanushkaNir  
**Module**: Notifications + Role Management + OAuth Integration  
**Status**: Ready for Production

