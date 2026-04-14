package com.campus.hub.repository;

import com.campus.hub.domain.AppRole;
import com.campus.hub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmailIgnoreCase(String email);

	boolean existsByEmailIgnoreCase(String email);

	List<User> findByRolesContains(AppRole role);
}
