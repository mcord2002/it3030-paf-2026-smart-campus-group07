package com.campus.hub.dto.ticket;

import com.campus.hub.domain.TicketPriority;
import com.campus.hub.domain.TicketStatus;
import com.campus.hub.dto.resource.ResourceResponse;

import java.time.Instant;

public record TicketResponse(
		Long id,
		Long reporterId,
		String reporterName,
		Long assigneeId,
		String assigneeName,
		ResourceResponse resource,
		String locationText,
		String category,
		String description,
		TicketPriority priority,
		TicketStatus status,
		String contactEmail,
		String contactPhone,
		String resolutionNotes,
		String rejectionReason,
		Instant createdAt,
		Instant updatedAt
) {
}
