package com.travelcrm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

public record UserRequestDto(
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    String username,

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    String password,

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    String email,

    @NotBlank(message = "First name cannot be blank")
    String firstName,

    @NotBlank(message = "Last name cannot be blank")
    String lastName,

    LocalDate dateOfBirth,

    String gender,

    String nationality,

    @Size(max = 20, message = "Passport number cannot exceed 20 characters")
    String passportNumber,

    LocalDate passportIssueDate,

    LocalDate passportExpiryDate,

    String passportImageUrl,

    @Pattern(regexp = "^\\+?[0-9.()-]{7,25}$", message = "Invalid phone number format")
    String phoneNumber,

    Set<String> roles
) {} 