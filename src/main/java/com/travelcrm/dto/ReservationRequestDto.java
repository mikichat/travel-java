package com.travelcrm.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ReservationRequestDto(
    @NotNull(message = "여행 ID는 필수입니다.")
    Long travelId,

    @NotNull(message = "사용자 ID는 필수입니다.")
    Long userId,

    Long itineraryId,

    @NotNull(message = "예약 날짜는 필수입니다.")
    @FutureOrPresent(message = "예약 날짜는 현재 또는 미래여야 합니다.")
    LocalDate reservationDate,

    @NotBlank(message = "상태는 필수입니다.")
    @Size(max = 50, message = "상태는 50자를 초과할 수 없습니다.")
    String status,

    @NotBlank(message = "Payment status cannot be blank")
    @Size(max = 50, message = "Payment status cannot exceed 50 characters")
    String paymentStatus,

    @NotNull(message = "Total amount cannot be null")
    Double totalAmount,

    @Size(max = 1000, message = "비고는 1000자를 초과할 수 없습니다.")
    String notes
) {} 