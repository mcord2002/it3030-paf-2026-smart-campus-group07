package com.campus.hub.dto.auth;

import com.campus.hub.domain.AppRole;

import java.util.Set;

public record AuthResponse(String token, UserSummary user) {

	public record UserSummary(Long id, String email, String fullName, Set<AppRole> roles) {
	}
}
