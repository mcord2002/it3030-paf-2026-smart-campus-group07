package com.campus.hub.dto.ticket;

import jakarta.validation.constraints.NotNull;

public record AssignTicketRequest(
		@NotNull Long assigneeUserId
) {
}
