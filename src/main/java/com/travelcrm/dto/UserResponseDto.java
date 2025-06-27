package com.travelcrm.dto;

import java.time.LocalDate;
import java.util.Set;

public record UserResponseDto(
    Long id,
    String username,
    String email,
    String firstName,
    String lastName,
    LocalDate dateOfBirth,
    String gender,
    String nationality,
    String passportNumber,
    LocalDate passportIssueDate,
    LocalDate passportExpiryDate,
    String passportImageUrl,
    String phoneNumber,
    Set<String> roles
) {} 