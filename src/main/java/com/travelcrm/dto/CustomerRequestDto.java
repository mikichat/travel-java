package com.travelcrm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CustomerRequestDto(
        @NotBlank(message = "이름은 필수입니다.")
        @Size(max = 100, message = "이름은 100자를 초과할 수 없습니다.")
        String firstName,

        @NotBlank(message = "성은 필수입니다.")
        @Size(max = 100, message = "성은 100자를 초과할 수 없습니다.")
        String lastName,

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "유효한 이메일 형식이어야 합니다.")
        @Size(max = 255, message = "이메일은 255자를 초과할 수 없습니다.")
        String email,

        @Size(max = 20, message = "전화번호는 20자를 초과할 수 없습니다.")
        String phone,

        @Size(max = 500, message = "주소는 500자를 초과할 수 없습니다.")
        String address,

        @Size(max = 50, message = "여권 번호는 50자를 초과할 수 없습니다.")
        String passportNumber,

        @PastOrPresent(message = "여권 발급일은 현재 또는 과거여야 합니다.")
        LocalDate passportIssueDate,

        LocalDate passportExpiryDate,

        @Size(max = 100, message = "여권 발행 국가는 100자를 초과할 수 없습니다.")
        String passportCountry,

        @Size(max = 2048, message = "여권 스캔 URL은 2048자를 초과할 수 없습니다.")
        String passportScanUrl,

        @Size(max = 1000, message = "비고는 1000자를 초과할 수 없습니다.")
        String notes
) {
} 