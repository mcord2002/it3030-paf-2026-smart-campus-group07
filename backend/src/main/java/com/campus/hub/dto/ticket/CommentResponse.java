package com.campus.hub.dto.ticket;

import java.time.Instant;

public record CommentResponse(
		Long id,
		Long ticketId,
		Long authorId,
		String authorName,
		String body,
		Instant createdAt,
		Instant updatedAt
) {
}
