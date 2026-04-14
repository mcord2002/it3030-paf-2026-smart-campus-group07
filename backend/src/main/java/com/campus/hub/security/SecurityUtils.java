package com.campus.hub.security;

import com.campus.hub.entity.User;
import com.campus.hub.exception.ApiException;
import com.campus.hub.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

	private final UserRepository userRepository;

	public SecurityUtils(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User currentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof HubUserDetails details)) {
			throw new ApiException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Not authenticated");
		}
		return userRepository.findByEmailIgnoreCase(details.getUsername())
				.orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "User not found"));
	}
}
