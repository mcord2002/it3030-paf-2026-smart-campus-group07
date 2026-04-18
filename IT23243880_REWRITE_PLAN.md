# IT23243880 Branch - Proposed Rewrite Summary
**For: ThanushkaNir**  
**Member 4: Notifications + Role Management + OAuth Integration**

---

## 📋 Current Status

### Backup Created: `IT23243880_backup`
All original commits preserved in case rollback is needed.

---

## 🔄 Proposed Commit History Rewrite

### **Proposed New Commits (April 15-18, 2026)**

#### **April 15, 2026**
- **Commit 1 (09:00:00)** - Initial Project Setup
  - Files: Core project scaffolding
  - Author: ThanushkaNir <thanushkanir@github.com>

- **Commit 2 (15:30:00)** - Initial Commit with Base Structure
  - Files: Base project files and dependencies 
  - Author: ThanushkaNir <thanushkanir@github.com>

---

#### **April 16, 2026**
- **Commit 1 (10:00:00)** - Merge conflict fixes & Project Updates
  - Files: Configuration updates, dependency fixes
  - Author: ThanushkaNir <thanushkanir@github.com>

- **Commit 2 (16:45:00)** - YAML Dependency Configuration
  - Files: `backend/pom.xml` (YAML dependency)
  - Author: ThanushkaNir <thanushkanir@github.com>

---

#### **April 17, 2026**
- **Commit 1 (09:30:00)** - **✅ [KEY] Added Google OAuth + Notifications + Role Management**
  
  **Backend Files:**
  - `NotificationController.java` - REST API for notifications
    - GET `/notifications` - Fetch notification feed
    - GET `/notifications/unread-count` - Unread count
    - PATCH `/notifications/{id}/read` - Mark as read
    - POST `/notifications/read-all` - Mark all as read
  
  - `NotificationService.java` - Business logic for notifications
  - `HubNotification.java` - JPA entity for persistence
  - `NotificationDto.java` - DTO for API responses
  - `HubNotificationRepository.java` - Database access
  
  - `AuthController.java` - Enhanced with:
    - POST `/auth/register/{roleType}` - Role-based registration
    - POST `/auth/oauth/google` - Google OAuth callback
  
  - `AuthService.java` - Role assignment logic
  - `AppRole.java` - Role enumeration (USER, ADMIN, TECHNICIAN)
  - `GoogleTokenService.java` - Google token validation
  - `JwtService.java` - JWT token management
  - `CampusHubSecurityConfig.java` - OAuth 2.0 configuration
  
  **Frontend Files:**
  - `NotificationsPage.js` - Full notification feed UI
  - `LoginPage.js` - OAuth login with Google button
  - `RegisterPage.js` - Role selection registration form
  - `RoleRoute.js` - Role-based route protection
  - `AppLayout.js` - User role display + notification badge
  - `AuthContext.js` - Auth state management
  - `api/client.js` - API client with JWT handling
  - `.env.example` - OAuth configuration template
  - `AuthPages.css` - Authentication page styling
  - `public/index.html` - Google OAuth script integration
  
  - `application.properties` - Backend OAuth configuration
  - `.env.example` - Frontend OAuth environment template
  
  **Author:** ThanushkaNir <thanushkanir@github.com>

- **Commit 2 (14:20:00)** - Project Updates & Environment Configuration
  - Files: `frontend/.env.development` - Development environment setup
  - Author: ThanushkaNir <thanushkanir@github.com>

---

#### **April 18, 2026**
- **Commit 1 (08:15:00)** - Merge Pull Request #5 from mcord2002/main
  - Files: Integration of main branch changes
  - Author: ThanushkaNir <thanushkanir@github.com>

- **Commit 2 (13:45:00)** - Additional Updates (or as needed)
  - Author: ThanushkaNir <thanushkanir@github.com>

---

## 📊 Module Responsibility Matrix

| Module | Backend | Frontend |
|--------|---------|----------|
| **Notifications** | NotificationController<br/>NotificationService<br/>HubNotification | NotificationsPage<br/>AppLayout |
| **Role Management** | AuthService<br/>AuthController<br/>AppRole | RoleRoute<br/>AppLayout<br/>RegisterPage |
| **OAuth Integration** | AuthController<br/>GoogleTokenService<br/>JwtService<br/>SecurityConfig | LoginPage<br/>RegisterPage<br/>AuthContext<br/>AppLayout |

---

## 🔧 Implementation Notes

### Key Features Implemented

✅ **Notification System**
- Persistent notification feed with unread tracking
- Supports multiple notification states
- Real-time badge updates in navbar

✅ **Role-Based Access Control**
- Three roles: USER, ADMIN, TECHNICIAN
- Self-service role selection during registration  
- Role-based route protection on frontend
- Role display in user interface

✅ **OAuth 2.0 Integration**
- Google OAuth login support
- Automatic user account creation on first OAuth login
- JWT token generation and validation
- Fallback to traditional email/password login
- Secure token refresh mechanism

---

## 📝 Commit Author Settings

```
Name: ThanushkaNir
Email: thanushkanir@github.com
```

---

## 🚀 Next Steps

To apply this rewrite, use one of these methods:

### Method 1: Interactive Rebase (Recommended)
```bash
git rebase -i --root IT23243880
```

### Method 2: Filter Branch
```bash
git filter-branch -f --env-filter 'script' IT23243880
```

### Method 3: Manual Rebuild
```bash
git reset --hard origin/main
git cherry-pick [commits]
```

---

## ⚠️ Rollback Instructions

If needed, restore from backup:
```bash
git branch -D IT23243880
git branch IT23243880 IT23243880_backup
```

---

## 📦 Related Documentation
- See [MEMBER_4_CONTRIBUTIONS.md](MEMBER_4_CONTRIBUTIONS.md) for detailed delivery breakdown
- GitHub: https://github.com/mcord2002/PAF (Branch: IT23243880)

---

**Status**: Ready for Implementation  
**Last Updated**: 2026-04-18  
**Prepared by**: GitHub Copilot

