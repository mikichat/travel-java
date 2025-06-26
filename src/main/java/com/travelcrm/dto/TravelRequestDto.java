package com.travelcrm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TravelRequestDto(
    @NotBlank(message = "Destination cannot be blank")
    String destination,
    @NotBlank(message = "Description cannot be blank")
    String description,
    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    Double price
) {
} 