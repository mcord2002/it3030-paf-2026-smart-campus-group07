package com.campus.hub.service;

import com.campus.hub.config.CampusHubProperties;
import com.campus.hub.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

	private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png", "image/webp");

	private final Path root;

	public FileStorageService(CampusHubProperties props) {
		try {
			this.root = Path.of(props.getUploadDir()).toAbsolutePath().normalize();
			Files.createDirectories(this.root);
		}
		catch (IOException e) {
			throw new IllegalStateException("Cannot initialize upload directory", e);
		}
	}

	public StoredFile saveTicketAttachment(long ticketId, MultipartFile file) throws IOException {
		String contentType = file.getContentType();
		if (contentType == null || !ALLOWED_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "INVALID_FILE_TYPE", "Only JPEG, PNG, or WEBP images are allowed.");
		}
		String original = file.getOriginalFilename() == null ? "image" : Path.of(file.getOriginalFilename()).getFileName().toString();
		if (original.length() > 180) {
			original = original.substring(0, 180);
		}
		String ext = extensionFor(contentType);
		String stored = UUID.randomUUID() + ext;
		Path dir = root.resolve("tickets").resolve(String.valueOf(ticketId));
		Files.createDirectories(dir);
		Path target = dir.resolve(stored).normalize();
		if (!target.startsWith(root)) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "INVALID_PATH", "Invalid storage path.");
		}
		try (InputStream in = file.getInputStream()) {
			Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
		}
		return new StoredFile(stored, original, contentType, file.getSize());
	}

	public Path resolveAttachmentPath(long ticketId, String storedFileName) {
		Path p = root.resolve("tickets").resolve(String.valueOf(ticketId)).resolve(storedFileName).normalize();
		if (!p.startsWith(root)) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "INVALID_PATH", "Invalid path.");
		}
		return p;
	}

	private static String extensionFor(String contentType) {
		return switch (contentType.toLowerCase(Locale.ROOT)) {
			case "image/png" -> ".png";
			case "image/webp" -> ".webp";
			default -> ".jpg";
		};
	}

	public record StoredFile(String storedFileName, String originalFileName, String contentType, long sizeBytes) {
	}
}
