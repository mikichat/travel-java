package com.travelcrm.service;

import com.travelcrm.dto.CustomerRequestDto;
import com.travelcrm.dto.CustomerResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerService {
    List<CustomerResponseDto> getAllCustomers();
    CustomerResponseDto getCustomerById(Long id);
    CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto);
    CustomerResponseDto updateCustomer(Long id, CustomerRequestDto customerRequestDto);
    void deleteCustomer(Long id);
    CustomerResponseDto processPassportOcr(MultipartFile passportImage) throws Exception;
} 