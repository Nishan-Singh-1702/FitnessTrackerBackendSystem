package com.Fitness.repository;

import com.Fitness.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(@NotBlank(message = "Email name cannot be blank") @Size(min = 5, message = "Email name must contain atleast 5 characters") @Email String email);

    boolean existsByEmail(String mail);
}
