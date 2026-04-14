package com.campus.hub.dto.ticket;

import com.campus.hub.domain.TicketPriority;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TicketCreateRequest(
		Long resourceId,
		@Size(max = 300) String locationText,
		@NotBlank @Size(max = 120) String category,
		@NotBlank @Size(max = 4000) String description,
		@NotNull TicketPriority priority,
		@Email @Size(max = 180) String contactEmail,
		@Size(max = 40) String contactPhone
) {
}
