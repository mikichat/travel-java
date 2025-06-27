package com.travelcrm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record VendorRequestDto(
    @NotBlank(message = "업체명은 필수 항목입니다.")
    @Size(max = 100, message = "업체명은 100자를 초과할 수 없습니다.")
    String name,

    @NotNull(message = "취급 항목은 필수입니다.")
    Set<String> handledItems,

    @Size(max = 100, message = "담당자명은 100자를 초과할 수 없습니다.")
    String contactPerson,

    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    @Size(max = 100, message = "담당자 이메일은 100자를 초과할 수 없습니다.")
    String contactEmail,

    @Size(max = 20, message = "담당자 전화번호는 20자를 초과할 수 없습니다.")
    String contactPhone,

    @Size(max = 255, message = "주소는 255자를 초과할 수 없습니다.")
    String address,

    @Size(max = 255, message = "웹사이트는 255자를 초과할 수 없습니다.")
    String website,

    String notes
) {
    public VendorRequestDto {
        if (handledItems == null || handledItems.isEmpty()) {
            throw new IllegalArgumentException("취급 항목은 최소 한 개 이상이어야 합니다.");
        }
    }
} 