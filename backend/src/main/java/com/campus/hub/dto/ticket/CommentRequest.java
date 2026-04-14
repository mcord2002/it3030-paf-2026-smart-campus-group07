package com.campus.hub.dto.ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
		@NotBlank @Size(max = 4000) String body
) {
}
