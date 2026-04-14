package com.campus.hub.service;

import com.campus.hub.domain.ResourceStatus;
import com.campus.hub.domain.ResourceType;
import com.campus.hub.dto.resource.ResourceRequest;
import com.campus.hub.dto.resource.ResourceResponse;
import com.campus.hub.entity.BookableResource;
import com.campus.hub.exception.ApiException;
import com.campus.hub.repository.BookableResourceRepository;
import com.campus.hub.repository.BookableResourceSpecifications;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResourceService {

	private final BookableResourceRepository resourceRepository;

	public ResourceService(BookableResourceRepository resourceRepository) {
		this.resourceRepository = resourceRepository;
	}

	@Transactional(readOnly = true)
	public List<ResourceResponse> search(String q, ResourceType type, Integer minCapacity, String location, ResourceStatus status) {
		Specification<BookableResource> spec = BookableResourceSpecifications.filter(q, type, minCapacity, location, status);
		return resourceRepository.findAll(spec, Sort.by(Sort.Direction.ASC, "name")).stream()
				.map(DtoMapper::toResource)
				.toList();
	}

	@Transactional(readOnly = true)
	public ResourceResponse get(Long id) {
		BookableResource r = resourceRepository.findById(id)
				.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", "Resource not found"));
		return DtoMapper.toResource(r);
	}

	@Transactional
	public ResourceResponse create(ResourceRequest req) {
		BookableResource r = new BookableResource();
		apply(r, req);
		return DtoMapper.toResource(resourceRepository.save(r));
	}

	@Transactional
	public ResourceResponse update(Long id, ResourceRequest req) {
		BookableResource r = resourceRepository.findById(id)
				.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", "Resource not found"));
		apply(r, req);
		return DtoMapper.toResource(resourceRepository.save(r));
	}

	@Transactional
	public void delete(Long id) {
		if (!resourceRepository.existsById(id)) {
			throw new ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", "Resource not found");
		}
		resourceRepository.deleteById(id);
	}

	private static void apply(BookableResource r, ResourceRequest req) {
		r.setName(req.name().trim());
		r.setType(req.type());
		r.setCapacity(req.capacity());
		r.setLocation(req.location().trim());
		r.setAvailabilityWindows(req.availabilityWindows());
		r.setStatus(req.status() != null ? req.status() : ResourceStatus.ACTIVE);
	}

	@Transactional(readOnly = true)
	public BookableResource require(Long id) {
		return resourceRepository.findById(id)
				.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", "Resource not found"));
	}
}
