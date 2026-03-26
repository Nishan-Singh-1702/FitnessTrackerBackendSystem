package com.Fitness.repository;

import com.Fitness.model.AppRole;
import com.Fitness.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRole(AppRole appRole);
}
