package com.travelcrm.dto;

import java.time.LocalDate;

public record CustomerResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String address,
        String passportNumber,
        LocalDate passportIssueDate,
        LocalDate passportExpiryDate,
        String passportCountry,
        String passportScanUrl,
        String notes
) {
} 