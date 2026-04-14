package com.campus.hub.dto.ticket;

import com.campus.hub.domain.TicketStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TicketStatusUpdateRequest(
		@NotNull TicketStatus status,
		@Size(max = 4000) String resolutionNotes
) {
}
