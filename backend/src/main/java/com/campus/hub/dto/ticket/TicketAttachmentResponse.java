package com.campus.hub.dto.ticket;

import java.time.Instant;

public record TicketAttachmentResponse(
		Long id,
		String originalFileName,
		String contentType,
		long sizeBytes,
		Long uploadedById,
		Instant createdAt
) {
}
