package com.campus.hub.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ErrorBody> handleApi(ApiException ex, HttpServletRequest req) {
		return ResponseEntity.status(ex.getStatus()).body(ErrorBody.of(req, ex.getStatus(), ex.getCode(), ex.getMessage(), null));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorBody> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
		Map<String, String> details = new HashMap<>();
		for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
			details.put(fe.getField(), fe.getDefaultMessage());
		}
		return ResponseEntity.badRequest()
				.body(ErrorBody.of(req, HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Validation failed", details));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorBody> handleConstraint(ConstraintViolationException ex, HttpServletRequest req) {
		String msg = ex.getConstraintViolations().stream()
				.map(v -> v.getPropertyPath() + ": " + v.getMessage())
				.collect(Collectors.joining("; "));
		return ResponseEntity.badRequest()
				.body(ErrorBody.of(req, HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", msg, null));
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorBody> handleBadCreds(BadCredentialsException ex, HttpServletRequest req) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(ErrorBody.of(req, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Invalid credentials", null));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorBody> handleAccess(AccessDeniedException ex, HttpServletRequest req) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(ErrorBody.of(req, HttpStatus.FORBIDDEN, "FORBIDDEN", "Access denied", null));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorBody> handleGeneric(Exception ex, HttpServletRequest req) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ErrorBody.of(req, HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Unexpected error", null));
	}

	public record ErrorBody(Instant timestamp, String path, int status, String code, String message, Map<String, ?> details) {
		static ErrorBody of(HttpServletRequest req, HttpStatus status, String code, String message, Map<String, ?> details) {
			return new ErrorBody(Instant.now(), req.getRequestURI(), status.value(), code, message, details);
		}
	}
}
