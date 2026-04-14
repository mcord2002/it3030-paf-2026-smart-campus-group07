package com.campus.hub.dto.notification;

import com.campus.hub.domain.NotificationType;

import java.time.Instant;

public record NotificationResponse(
		Long id,
		NotificationType type,
		String title,
		String message,
		Long relatedEntityId,
		boolean read,
		Instant createdAt
) {
}
