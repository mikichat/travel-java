package com.travelcrm.service;

import com.travelcrm.dto.AuthResponseDto;
import com.travelcrm.dto.UserLoginRequestDto;
import com.travelcrm.dto.UserRegisterRequestDto;

public interface AuthService {
    AuthResponseDto registerUser(UserRegisterRequestDto registerRequest);
    AuthResponseDto loginUser(UserLoginRequestDto loginRequest);
} 