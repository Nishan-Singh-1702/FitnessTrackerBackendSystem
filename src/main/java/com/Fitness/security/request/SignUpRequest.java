package com.Fitness.security.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "First name cannot be blank")
    @Size(min = 2, message = "First name must contain atleast 2 characters")
    private String firstName;

    @NotBlank(message = "last name cannot be blank")
    @Size(min = 2, message = "last name must contain atleast 2 characters")
    private String lastName;

    @NotBlank(message = "Email name cannot be blank")
    @Size(min = 5, message = "Email name must contain atleast 5 characters")
    @Email
    @Column(unique = true,nullable = false)
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password length must be atleast 6 characters")
    private String password;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private Set<String> role;
}
