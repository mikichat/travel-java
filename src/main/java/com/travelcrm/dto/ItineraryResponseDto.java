package com.travelcrm.dto;

import java.time.LocalDate;

public record ItineraryResponseDto(
    Long id,
    Long travelId,
    Long vendorId,
    String activityName,
    LocalDate activityDate,
    String description,
    String location
) {} 