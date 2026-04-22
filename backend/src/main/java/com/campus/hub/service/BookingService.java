package com.campus.hub.service;

import com.campus.hub.domain.AppRole;
import com.campus.hub.domain.BookingStatus;
import com.campus.hub.domain.NotificationType;
import com.campus.hub.domain.ResourceStatus;
import com.campus.hub.dto.booking.BookingCreateRequest;
import com.campus.hub.dto.booking.BookingResponse;
import com.campus.hub.dto.booking.RejectBookingRequest;
import com.campus.hub.entity.BookableResource;
import com.campus.hub.entity.Booking;
import com.campus.hub.entity.User;
import com.campus.hub.exception.ApiException;
import com.campus.hub.repository.BookingRepository;
import com.campus.hub.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class BookingService {

	private static final List<BookingStatus> BLOCKING_STATUSES = List.of(BookingStatus.PENDING, BookingStatus.APPROVED);

	private final BookingRepository bookingRepository;
	private final ResourceService resourceService;
	private final NotificationService notificationService;
	private final UserRepository userRepository;

	public BookingService(
			BookingRepository bookingRepository,
			ResourceService resourceService,
			NotificationService notificationService,
			UserRepository userRepository) {
		this.bookingRepository = bookingRepository;
		this.resourceService = resourceService;
		this.notificationService = notificationService;
		this.userRepository = userRepository;
	}

	@Transactional
	public BookingResponse create(User requester, BookingCreateRequest req) {
		if (!req.endAt().isAfter(req.startAt())) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "INVALID_RANGE", "End time must be after start time.");
		}
		if (req.startAt().isBefore(Instant.now())) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "INVALID_START", "Start time cannot be in the past.");
		}
		BookableResource resource = resourceService.require(req.resourceId());
		if (resource.getStatus() != ResourceStatus.ACTIVE) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "RESOURCE_UNAVAILABLE", "Resource is not bookable.");
		}
		assertNoOverlap(resource.getId(), req.startAt(), req.endAt(), null);

		Booking b = new Booking();
		b.setRequester(requester);
		b.setResource(resource);
		b.setStartAt(req.startAt());
		b.setEndAt(req.endAt());
		b.setPurpose(req.purpose().trim());
		b.setExpectedAttendees(req.expectedAttendees());
		b.setStatus(BookingStatus.PENDING);
		b = bookingRepository.save(b);
		notifyAdminsOfNewBooking(b);
		return DtoMapper.toBooking(b);
	}

	@Transactional(readOnly = true)
	public List<BookingResponse> mine(User user) {
		return bookingRepository.findByRequesterIdOrderByStartAtDesc(user.getId()).stream()
				.map(DtoMapper::toBooking)
				.toList();
	}

	@Transactional(readOnly = true)
	public List<BookingResponse> listAll(User admin, BookingStatus status, Long resourceId) {
		requireRole(admin, AppRole.ADMIN);
		return bookingRepository.findAll().stream()
				.filter(b -> status == null || b.getStatus() == status)
				.filter(b -> resourceId == null || Objects.equals(b.getResource().getId(), resourceId))
				.sorted(Comparator.comparing(Booking::getCreatedAt).reversed())
				.map(DtoMapper::toBooking)
				.toList();
	}

	@Transactional
	public BookingResponse approve(User admin, Long bookingId) {
		requireRole(admin, AppRole.ADMIN);
		Booking b = getBooking(bookingId);
		if (b.getStatus() != BookingStatus.PENDING) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "INVALID_STATE", "Only pending bookings can be approved.");
		}
		assertNoOverlap(b.getResource().getId(), b.getStartAt(), b.getEndAt(), b.getId());
		b.setStatus(BookingStatus.APPROVED);
		b.setReviewedBy(admin);
		b.setReviewedAt(Instant.now());
		b.setAdminReason(null);
		bookingRepository.save(b);
		notificationService.notifyUser(
				b.getRequester(),
				NotificationType.BOOKING_DECISION,
				"Booking approved",
				"Your booking for " + b.getResource().getName() + " was approved.",
				b.getId());
		return DtoMapper.toBooking(b);
	}

	@Transactional
	public BookingResponse reject(User admin, Long bookingId, RejectBookingRequest req) {
		requireRole(admin, AppRole.ADMIN);
		Booking b = getBooking(bookingId);
		if (b.getStatus() != BookingStatus.PENDING) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "INVALID_STATE", "Only pending bookings can be rejected.");
		}
		b.setStatus(BookingStatus.REJECTED);
		b.setAdminReason(req.reason().trim());
		b.setReviewedBy(admin);
		b.setReviewedAt(Instant.now());
		bookingRepository.save(b);
		notificationService.notifyUser(
				b.getRequester(),
				NotificationType.BOOKING_DECISION,
				"Booking rejected",
				"Your booking for " + b.getResource().getName() + " was rejected. Reason: " + req.reason().trim(),
				b.getId());
		return DtoMapper.toBooking(b);
	}

	@Transactional
	public BookingResponse cancel(User actor, Long bookingId) {
		Booking b = getBooking(bookingId);
		boolean owner = Objects.equals(b.getRequester().getId(), actor.getId());
		boolean admin = actor.getRoles().contains(AppRole.ADMIN);
		if (!owner && !admin) {
			throw new ApiException(HttpStatus.FORBIDDEN, "FORBIDDEN", "You cannot cancel this booking.");
		}
		if (b.getStatus() != BookingStatus.APPROVED && b.getStatus() != BookingStatus.PENDING) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "INVALID_STATE", "Only pending or approved bookings can be cancelled.");
		}
		b.setStatus(BookingStatus.CANCELLED);
		bookingRepository.save(b);
		return DtoMapper.toBooking(b);
	}

	private void assertNoOverlap(Long resourceId, Instant start, Instant end, Long excludeId) {
		long count = bookingRepository.countOverlapping(resourceId, BLOCKING_STATUSES, start, end, excludeId);
		if (count > 0) {
			throw new ApiException(HttpStatus.CONFLICT, "SCHEDULE_CONFLICT", "This time range overlaps an existing booking.");
		}
	}

	private Booking getBooking(Long id) {
		return bookingRepository.findById(id)
				.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", "Booking not found"));
	}

	private static void requireRole(User u, AppRole role) {
		if (!u.getRoles().contains(role)) {
			throw new ApiException(HttpStatus.FORBIDDEN, "FORBIDDEN", "Admin access required.");
		}
	}

	private void notifyAdminsOfNewBooking(Booking booking) {
		List<User> admins = userRepository.findByRolesContains(AppRole.ADMIN);
		for (User admin : admins) {
			notificationService.notifyUser(
					admin,
					NotificationType.BOOKING_DECISION,
					"New booking request",
					"Booking #" + booking.getId() + " is pending review for " + booking.getResource().getName() + ".",
					booking.getId());
		}
	}
}
