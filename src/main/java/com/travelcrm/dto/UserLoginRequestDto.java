package com.travelcrm.dto;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestDto(
        @NotBlank(message = "Username cannot be blank")
        String username,

        @NotBlank(message = "Password cannot be blank")
        String password
) {} 