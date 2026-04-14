package com.campus.hub.repository;

import com.campus.hub.entity.IncidentTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentTicketRepository extends JpaRepository<IncidentTicket, Long> {

	List<IncidentTicket> findByReporterIdOrderByUpdatedAtDesc(Long reporterId);

	List<IncidentTicket> findByAssigneeIdOrderByUpdatedAtDesc(Long assigneeId);
}
