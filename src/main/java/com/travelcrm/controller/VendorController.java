package com.travelcrm.controller;

import com.travelcrm.dto.VendorRequestDto;
import com.travelcrm.dto.VendorResponseDto;
import com.travelcrm.service.VendorService;
import com.travelcrm.util.ApiResponse;
import com.travelcrm.util.GlobalExceptionHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping
    public ResponseEntity<ApiResponse<VendorResponseDto>> createVendor(@Valid @RequestBody VendorRequestDto vendorRequestDto) {
        try {
            VendorResponseDto createdVendor = vendorService.createVendor(vendorRequestDto);
            return new ResponseEntity<>(new ApiResponse<>("SUCCESS", "업체가 성공적으로 생성되었습니다.", createdVendor), HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return GlobalExceptionHandler.<VendorResponseDto>errorResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return GlobalExceptionHandler.<VendorResponseDto>errorResponseEntity("업체 생성 중 오류가 발생했습니다: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VendorResponseDto>>> getAllVendors() {
        try {
            List<VendorResponseDto> vendors = vendorService.getAllVendors();
            return new ResponseEntity<>(new ApiResponse<>("SUCCESS", "모든 업체가 성공적으로 조회되었습니다.", vendors), HttpStatus.OK);
        } catch (Exception ex) {
            return GlobalExceptionHandler.<List<VendorResponseDto>>errorResponseEntity("업체 목록 조회 중 오류가 발생했습니다: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VendorResponseDto>> getVendorById(@PathVariable Long id) {
        try {
            VendorResponseDto vendor = vendorService.getVendorById(id);
            return new ResponseEntity<>(new ApiResponse<>("SUCCESS", "업체가 성공적으로 조회되었습니다.", vendor), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return GlobalExceptionHandler.<VendorResponseDto>errorResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return GlobalExceptionHandler.<VendorResponseDto>errorResponseEntity("업체 조회 중 오류가 발생했습니다: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VendorResponseDto>> updateVendor(@PathVariable Long id, @Valid @RequestBody VendorRequestDto vendorRequestDto) {
        try {
            VendorResponseDto updatedVendor = vendorService.updateVendor(id, vendorRequestDto);
            return new ResponseEntity<>(new ApiResponse<>("SUCCESS", "업체가 성공적으로 업데이트되었습니다.", updatedVendor), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return GlobalExceptionHandler.<VendorResponseDto>errorResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return GlobalExceptionHandler.<VendorResponseDto>errorResponseEntity("업체 업데이트 중 오류가 발생했습니다: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteVendor(@PathVariable Long id) {
        try {
            vendorService.deleteVendor(id);
            return new ResponseEntity<>(new ApiResponse<>("SUCCESS", "업체가 성공적으로 삭제되었습니다.", null), HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException ex) {
            ApiResponse<?> response = new ApiResponse<>("ERROR", ex.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            ApiResponse<?> response = new ApiResponse<>("ERROR", "업체 삭제 중 오류가 발생했습니다: " + ex.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 