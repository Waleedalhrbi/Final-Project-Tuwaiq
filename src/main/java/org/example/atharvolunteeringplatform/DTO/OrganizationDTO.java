package org.example.atharvolunteeringplatform.DTO;


import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor

public class OrganizationDTO {

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Username cannot be null")
    @Size(min = 4, max = 10, message = "Username must be between 4 and 10 characters")
    private String username;

    @NotEmpty(message = "Organization name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotEmpty(message = "Phone number is required")
    @Pattern(regexp = "^05\\d{8}$", message = "Phone number must start with 05 and be 10 digits")
    private String phoneNumber;

    @NotEmpty(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotEmpty(message = "License number is required")
    private String license;

    @NotEmpty(message = "Location is required")
    private String location;
}
