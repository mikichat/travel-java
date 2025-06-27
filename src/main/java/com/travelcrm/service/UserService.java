package com.travelcrm.service;

import com.travelcrm.dto.UserRequestDto;
import com.travelcrm.dto.UserResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userRequestDto);
    UserResponseDto getUserById(Long id);
    List<UserResponseDto> getAllUsers();
    UserResponseDto updateUser(Long id, UserRequestDto userRequestDto);
    void deleteUser(Long id);
    UserResponseDto uploadPassportImageAndExtractInfo(Long userId, MultipartFile passportImage) throws Exception;
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
} 