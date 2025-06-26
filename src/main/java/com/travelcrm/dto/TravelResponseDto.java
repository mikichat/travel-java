package com.travelcrm.dto;

public record TravelResponseDto(
    Long id,
    String destination,
    String description,
    Double price
) {
} 