package com.travelcrm.service.impl;

import com.travelcrm.dto.VendorRequestDto;
import com.travelcrm.dto.VendorResponseDto;
import com.travelcrm.entity.Vendor;
import com.travelcrm.repository.VendorRepository;
import com.travelcrm.service.VendorService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Override
    @Transactional
    public VendorResponseDto createVendor(VendorRequestDto vendorRequestDto) {
        Vendor vendor = new Vendor();
        BeanUtils.copyProperties(vendorRequestDto, vendor);
        Vendor savedVendor = vendorRepository.save(vendor);
        return new VendorResponseDto(
                savedVendor.getId(),
                savedVendor.getName(),
                savedVendor.getHandledItems(),
                savedVendor.getContactPerson(),
                savedVendor.getContactEmail(),
                savedVendor.getContactPhone(),
                savedVendor.getAddress(),
                savedVendor.getWebsite(),
                savedVendor.getNotes()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public VendorResponseDto getVendorById(Long id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 업체를 찾을 수 없습니다: " + id));
        return new VendorResponseDto(
                vendor.getId(),
                vendor.getName(),
                vendor.getHandledItems(),
                vendor.getContactPerson(),
                vendor.getContactEmail(),
                vendor.getContactPhone(),
                vendor.getAddress(),
                vendor.getWebsite(),
                vendor.getNotes()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<VendorResponseDto> getAllVendors() {
        return vendorRepository.findAll().stream()
                .map(vendor -> new VendorResponseDto(
                        vendor.getId(),
                        vendor.getName(),
                        vendor.getHandledItems(),
                        vendor.getContactPerson(),
                        vendor.getContactEmail(),
                        vendor.getContactPhone(),
                        vendor.getAddress(),
                        vendor.getWebsite(),
                        vendor.getNotes()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VendorResponseDto updateVendor(Long id, VendorRequestDto vendorRequestDto) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 업체를 찾을 수 없습니다: " + id));
        
        vendor.setName(vendorRequestDto.name());
        vendor.setHandledItems(vendorRequestDto.handledItems());
        vendor.setContactPerson(vendorRequestDto.contactPerson());
        vendor.setContactEmail(vendorRequestDto.contactEmail());
        vendor.setContactPhone(vendorRequestDto.contactPhone());
        vendor.setAddress(vendorRequestDto.address());
        vendor.setWebsite(vendorRequestDto.website());
        vendor.setNotes(vendorRequestDto.notes());

        Vendor updatedVendor = vendorRepository.save(vendor);
        return new VendorResponseDto(
                updatedVendor.getId(),
                updatedVendor.getName(),
                updatedVendor.getHandledItems(),
                updatedVendor.getContactPerson(),
                updatedVendor.getContactEmail(),
                updatedVendor.getContactPhone(),
                updatedVendor.getAddress(),
                updatedVendor.getWebsite(),
                updatedVendor.getNotes()
        );
    }

    @Override
    @Transactional
    public void deleteVendor(Long id) {
        if (!vendorRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 ID의 업체를 찾을 수 없습니다: " + id);
        }
        vendorRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return vendorRepository.existsById(id);
    }
} 