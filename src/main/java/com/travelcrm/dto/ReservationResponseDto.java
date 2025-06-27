package com.travelcrm.dto;

import java.time.LocalDate;

public record ReservationResponseDto(
    Long id,
    Long travelId,
    String travelName,
    Long userId,
    String userName,
    LocalDate reservationDate,
    String status,
    String notes,
    String qrCodeUrl
) {} 