package com.campus.hub.entity;

import com.campus.hub.domain.ResourceStatus;
import com.campus.hub.domain.ResourceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bookable_resources")
public class BookableResource {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 200)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 32)
	private ResourceType type;

	@Column(nullable = false)
	private Integer capacity;

	@Column(nullable = false, length = 300)
	private String location;

	/** Optional JSON or human text describing availability windows */
	@Column(name = "availability_windows", length = 2000)
	private String availabilityWindows;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 32)
	private ResourceStatus status = ResourceStatus.ACTIVE;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ResourceType getType() {
		return type;
	}

	public void setType(ResourceType type) {
		this.type = type;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAvailabilityWindows() {
		return availabilityWindows;
	}

	public void setAvailabilityWindows(String availabilityWindows) {
		this.availabilityWindows = availabilityWindows;
	}

	public ResourceStatus getStatus() {
		return status;
	}

	public void setStatus(ResourceStatus status) {
		this.status = status;
	}
}
