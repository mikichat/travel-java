package com.travelcrm.service.impl;

import com.travelcrm.dto.AuthResponseDto;
import com.travelcrm.dto.UserLoginRequestDto;
import com.travelcrm.dto.UserRegisterRequestDto;
import com.travelcrm.entity.User;
import com.travelcrm.repository.UserRepository;
import com.travelcrm.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AuthResponseDto registerUser(UserRegisterRequestDto registerRequest) {
        if (userRepository.existsByUsername(registerRequest.username())) {
            throw new IllegalArgumentException("Username is already taken!");
        }

        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new IllegalArgumentException("Email is already in use!");
        }

        User user = new User();
        user.setUsername(registerRequest.username());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setEmail(registerRequest.email());

        userRepository.save(user);

        // TODO: JWT 토큰 생성 및 반환 로직 추가 예정
        return new AuthResponseDto("dummy_token_for_registration", user.getUsername());
    }

    @Override
    public AuthResponseDto loginUser(UserLoginRequestDto loginRequest) {
        // TODO: Spring Security AuthenticationManager를 이용한 로그인 로직 및 JWT 토큰 생성 로직 추가 예정
        throw new UnsupportedOperationException("Login functionality not yet implemented.");
    }
} 