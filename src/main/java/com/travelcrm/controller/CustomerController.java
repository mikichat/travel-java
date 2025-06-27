package com.travelcrm.controller;

import com.travelcrm.dto.CustomerRequestDto;
import com.travelcrm.dto.CustomerResponseDto;
import com.travelcrm.service.CustomerService;
import com.travelcrm.util.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerResponseDto>>> getAllCustomers() {
        try {
            List<CustomerResponseDto> customers = customerService.getAllCustomers();
            return new ResponseEntity<>(new ApiResponse<>("SUCCESS", "고객 목록을 성공적으로 불러왔습니다.", customers), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>("ERROR", "고객 목록을 불러오는 중 오류가 발생했습니다: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponseDto>> getCustomerById(@PathVariable Long id) {
        try {
            CustomerResponseDto customer = customerService.getCustomerById(id);
            return new ResponseEntity<>(new ApiResponse<>("SUCCESS", "고객을 성공적으로 불러왔습니다.", customer), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>("ERROR", "고객을 불러오는 중 오류가 발생했습니다: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponseDto>> createCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDto) {
        try {
            CustomerResponseDto createdCustomer = customerService.createCustomer(customerRequestDto);
            return new ResponseEntity<>(new ApiResponse<>("SUCCESS", "고객을 성공적으로 생성했습니다.", createdCustomer), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>("ERROR", "고객 생성 중 오류가 발생했습니다: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponseDto>> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerRequestDto customerRequestDto) {
        try {
            CustomerResponseDto updatedCustomer = customerService.updateCustomer(id, customerRequestDto);
            return new ResponseEntity<>(new ApiResponse<>("SUCCESS", "고객을 성공적으로 업데이트했습니다.", updatedCustomer), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>("ERROR", "고객 업데이트 중 오류가 발생했습니다: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return new ResponseEntity<>(new ApiResponse<>("SUCCESS", "고객을 성공적으로 삭제했습니다.", null), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>("ERROR", "고객 삭제 중 오류가 발생했습니다: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/ocr")
    public ResponseEntity<ApiResponse<CustomerResponseDto>> uploadPassportForOcr(@RequestParam("file") MultipartFile file) {
        try {
            CustomerResponseDto customerData = customerService.processPassportOcr(file);
            return new ResponseEntity<>(new ApiResponse<>("SUCCESS", "여권 OCR 처리가 성공적으로 완료되었습니다.", customerData), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>("ERROR", "여권 OCR 처리 중 오류가 발생했습니다: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 