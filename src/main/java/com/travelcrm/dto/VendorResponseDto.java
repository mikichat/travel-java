package com.travelcrm.dto;

import java.util.Set;

public record VendorResponseDto(
    Long id,
    String name,
    Set<String> handledItems,
    String contactPerson,
    String contactEmail,
    String contactPhone,
    String address,
    String website,
    String notes
) {
} 