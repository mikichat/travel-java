package com.travelcrm.controller;

import com.travelcrm.dto.AuthResponseDto;
import com.travelcrm.dto.UserLoginRequestDto;
import com.travelcrm.dto.UserRegisterRequestDto;
import com.travelcrm.service.AuthService;
import com.travelcrm.util.ApiResponse;
import com.travelcrm.util.GlobalExceptionHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> registerUser(@Valid @RequestBody UserRegisterRequestDto registerRequest) {
        try {
            AuthResponseDto authResponse = authService.registerUser(registerRequest);
            return ResponseEntity.ok(ApiResponse.success("User registered successfully", authResponse));
        } catch (IllegalArgumentException ex) {
            return GlobalExceptionHandler.errorResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return GlobalExceptionHandler.errorResponseEntity("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> loginUser(@Valid @RequestBody UserLoginRequestDto loginRequest) {
        try {
            AuthResponseDto authResponse = authService.loginUser(loginRequest);
            return ResponseEntity.ok(ApiResponse.success("User logged in successfully", authResponse));
        } catch (IllegalArgumentException ex) {
            return GlobalExceptionHandler.errorResponseEntity(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            return GlobalExceptionHandler.errorResponseEntity("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 