package com.travelcrm.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ReservationRequestDto(
    @NotNull(message = "Travel ID cannot be null")
    Long travelId,

    @NotNull(message = "User ID cannot be null")
    Long userId,

    Long itineraryId,

    @NotNull(message = "Reservation date cannot be null")
    LocalDate reservationDate,

    @NotBlank(message = "Status cannot be blank")
    @Size(max = 50, message = "Status cannot exceed 50 characters")
    String status,

    @NotBlank(message = "Payment status cannot be blank")
    @Size(max = 50, message = "Payment status cannot exceed 50 characters")
    String paymentStatus,

    @NotNull(message = "Total amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total amount must be positive")
    Double totalAmount,

    String notes
) {} 