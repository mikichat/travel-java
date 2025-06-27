package com.travelcrm.service;

import com.travelcrm.dto.VendorRequestDto;
import com.travelcrm.dto.VendorResponseDto;

import java.util.List;

public interface VendorService {
    VendorResponseDto createVendor(VendorRequestDto vendorRequestDto);
    VendorResponseDto getVendorById(Long id);
    List<VendorResponseDto> getAllVendors();
    VendorResponseDto updateVendor(Long id, VendorRequestDto vendorRequestDto);
    void deleteVendor(Long id);
    boolean existsById(Long id);
} 