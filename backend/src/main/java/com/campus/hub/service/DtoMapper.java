package com.campus.hub.service;

import com.campus.hub.dto.booking.BookingResponse;
import com.campus.hub.dto.notification.NotificationResponse;
import com.campus.hub.dto.resource.ResourceResponse;
import com.campus.hub.dto.ticket.CommentResponse;
import com.campus.hub.dto.ticket.TicketAttachmentResponse;
import com.campus.hub.dto.ticket.TicketResponse;
import com.campus.hub.entity.BookableResource;
import com.campus.hub.entity.Booking;
import com.campus.hub.entity.HubNotification;
import com.campus.hub.entity.IncidentTicket;
import com.campus.hub.entity.TicketAttachment;
import com.campus.hub.entity.TicketComment;
import com.campus.hub.entity.User;

public final class DtoMapper {

	private DtoMapper() {
	}

	public static ResourceResponse toResource(BookableResource r) {
		return new ResourceResponse(
				r.getId(),
				r.getName(),
				r.getType(),
				r.getCapacity(),
				r.getLocation(),
				r.getAvailabilityWindows(),
				r.getStatus());
	}

	public static BookingResponse toBooking(Booking b) {
		User req = b.getRequester();
		BookableResource res = b.getResource();
		return new BookingResponse(
				b.getId(),
				req.getId(),
				req.getFullName(),
				toResource(res),
				b.getStartAt(),
				b.getEndAt(),
				b.getPurpose(),
				b.getExpectedAttendees(),
				b.getStatus(),
				b.getAdminReason(),
				b.getReviewedBy() != null ? b.getReviewedBy().getId() : null,
				b.getReviewedAt(),
				b.getCreatedAt());
	}

	public static TicketResponse toTicket(IncidentTicket t) {
		return new TicketResponse(
				t.getId(),
				t.getReporter().getId(),
				t.getReporter().getFullName(),
				t.getAssignee() != null ? t.getAssignee().getId() : null,
				t.getAssignee() != null ? t.getAssignee().getFullName() : null,
				t.getResource() != null ? toResource(t.getResource()) : null,
				t.getLocationText(),
				t.getCategory(),
				t.getDescription(),
				t.getPriority(),
				t.getStatus(),
				t.getContactEmail(),
				t.getContactPhone(),
				t.getResolutionNotes(),
				t.getRejectionReason(),
				t.getCreatedAt(),
				t.getUpdatedAt());
	}

	public static TicketAttachmentResponse toAttachment(TicketAttachment a) {
		return new TicketAttachmentResponse(
				a.getId(),
				a.getOriginalFileName(),
				a.getContentType(),
				a.getSizeBytes(),
				a.getUploadedBy().getId(),
				a.getCreatedAt());
	}

	public static CommentResponse toComment(TicketComment c) {
		return new CommentResponse(
				c.getId(),
				c.getTicket().getId(),
				c.getAuthor().getId(),
				c.getAuthor().getFullName(),
				c.getBody(),
				c.getCreatedAt(),
				c.getUpdatedAt());
	}

	public static NotificationResponse toNotification(HubNotification n) {
		return new NotificationResponse(
				n.getId(),
				n.getType(),
				n.getTitle(),
				n.getMessage(),
				n.getRelatedEntityId(),
				n.isReadFlag(),
				n.getCreatedAt());
	}
}
