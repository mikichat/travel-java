package com.travelcrm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ItineraryRequestDto(
    @NotNull(message = "Travel ID cannot be null")
    Long travelId,

    Long vendorId,

    @NotBlank(message = "Activity name cannot be blank")
    @Size(max = 255, message = "Activity name cannot exceed 255 characters")
    String activityName,

    @NotNull(message = "Activity date cannot be null")
    LocalDate activityDate,

    String description,

    @NotBlank(message = "Location cannot be blank")
    @Size(max = 255, message = "Location cannot exceed 255 characters")
    String location
) {} 