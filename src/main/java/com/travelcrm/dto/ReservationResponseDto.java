package com.travelcrm.dto;

import java.time.LocalDate;

public record ReservationResponseDto(
    Long id,
    String reservationCode,
    Long travelId,
    Long userId,
    Long itineraryId,
    LocalDate reservationDate,
    String status,
    String paymentStatus,
    Double totalAmount,
    String notes
) {} 