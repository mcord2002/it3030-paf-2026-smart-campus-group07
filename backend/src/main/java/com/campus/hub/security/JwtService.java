package com.campus.hub.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

	private final String secret;
	private final long expirationMs;

	public JwtService(
			@Value("${campus-hub.jwt.secret}") String secret,
			@Value("${campus-hub.jwt.expiration-ms:86400000}") long expirationMs) {
		this.secret = secret;
		this.expirationMs = expirationMs;
	}

	public String generateToken(String subjectEmail) {
		Date now = new Date();
		Date exp = new Date(now.getTime() + expirationMs);
		return Jwts.builder()
				.subject(subjectEmail)
				.issuedAt(now)
				.expiration(exp)
				.signWith(signingKey())
				.compact();
	}

	public String extractEmail(String token) {
		return parseClaims(token).getSubject();
	}

	public boolean isTokenValid(String token, String expectedEmail) {
		try {
			Claims claims = parseClaims(token);
			return expectedEmail.equalsIgnoreCase(claims.getSubject()) && claims.getExpiration().after(new Date());
		}
		catch (RuntimeException ex) {
			return false;
		}
	}

	private Claims parseClaims(String token) {
		return Jwts.parser()
				.verifyWith(signingKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	private SecretKey signingKey() {
		byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
