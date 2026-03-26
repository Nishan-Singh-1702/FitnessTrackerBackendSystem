package com.Fitness.payload;

import com.Fitness.model.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long userId;

    @NotBlank(message = "First name cannot be blank")
    @Size(min = 2, message = "First name must contain atleast 2 characters")
    private String firstName;

    @NotBlank(message = "last name cannot be blank")
    @Size(min = 2, message = "last name must contain atleast 2 characters")
    private String lastName;

    @NotBlank(message = "Email name cannot be blank")
    @Size(min = 5, message = "Email name must contain atleast 5 characters")
    @Email
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password length must be atleast 6 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<String> roles;

}
