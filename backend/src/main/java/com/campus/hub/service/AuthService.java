package com.campus.hub.service;

import com.campus.hub.domain.AppRole;
import com.campus.hub.dto.auth.AuthResponse;
import com.campus.hub.dto.auth.LoginRequest;
import com.campus.hub.dto.auth.RegisterRequest;
import com.campus.hub.entity.User;
import com.campus.hub.exception.ApiException;
import com.campus.hub.repository.UserRepository;
import com.campus.hub.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public AuthService(
			UserRepository userRepository,
			PasswordEncoder passwordEncoder,
			JwtService jwtService,
			AuthenticationManager authenticationManager) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

	@Transactional
	public AuthResponse register(RegisterRequest req) {
		if (userRepository.existsByEmailIgnoreCase(req.email())) {
			throw new ApiException(HttpStatus.CONFLICT, "EMAIL_IN_USE", "Email is already registered.");
		}
		AppRole requested = req.accountType() != null ? req.accountType() : AppRole.USER;
		Set<AppRole> roles = switch (requested) {
			case TECHNICIAN -> Set.of(AppRole.TECHNICIAN);
			case ADMIN -> Set.of(AppRole.ADMIN);
			case USER -> Set.of(AppRole.USER);
			default -> throw new ApiException(HttpStatus.BAD_REQUEST, "INVALID_ROLE", "Unsupported account type.");
		};
		User u = new User();
		u.setEmail(req.email().trim().toLowerCase());
		u.setPasswordHash(passwordEncoder.encode(req.password()));
		u.setFullName(req.fullName().trim());
		u.setPhone(req.phone());
		u.setRoles(roles);
		userRepository.save(u);
		String token = jwtService.generateToken(u.getEmail());
		return new AuthResponse(token, new AuthResponse.UserSummary(u.getId(), u.getEmail(), u.getFullName(), u.getRoles()));
	}

	@Transactional(readOnly = true)
	public AuthResponse login(LoginRequest req) {
		String email = req.email().trim().toLowerCase();
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, req.password()));
		User u = userRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Invalid credentials"));
		String token = jwtService.generateToken(u.getEmail());
		return new AuthResponse(token, new AuthResponse.UserSummary(u.getId(), u.getEmail(), u.getFullName(), u.getRoles()));
	}
}
