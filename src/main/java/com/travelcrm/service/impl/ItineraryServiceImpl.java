package com.travelcrm.service.impl;

import com.travelcrm.dto.ItineraryRequestDto;
import com.travelcrm.dto.ItineraryResponseDto;
import com.travelcrm.entity.Itinerary;
import com.travelcrm.entity.Travel;
import com.travelcrm.entity.Vendor;
import com.travelcrm.repository.ItineraryRepository;
import com.travelcrm.repository.TravelRepository;
import com.travelcrm.repository.VendorRepository;
import com.travelcrm.service.ItineraryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItineraryServiceImpl implements ItineraryService {

    @Autowired
    private ItineraryRepository itineraryRepository;

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Override
    @Transactional
    public ItineraryResponseDto createItinerary(ItineraryRequestDto itineraryRequestDto) {
        Travel travel = travelRepository.findById(itineraryRequestDto.travelId())
                .orElseThrow(() -> new IllegalArgumentException("Travel not found with ID: " + itineraryRequestDto.travelId()));

        Vendor vendor = null;
        if (itineraryRequestDto.vendorId() != null) {
            vendor = vendorRepository.findById(itineraryRequestDto.vendorId())
                    .orElseThrow(() -> new IllegalArgumentException("Vendor not found with ID: " + itineraryRequestDto.vendorId()));
        }

        Itinerary itinerary = new Itinerary();
        BeanUtils.copyProperties(itineraryRequestDto, itinerary);
        itinerary.setTravel(travel);
        itinerary.setVendor(vendor);

        Itinerary savedItinerary = itineraryRepository.save(itinerary);
        return convertToDto(savedItinerary);
    }

    @Override
    public ItineraryResponseDto getItineraryById(Long id) {
        Itinerary itinerary = itineraryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Itinerary not found with ID: " + id));
        return convertToDto(itinerary);
    }

    @Override
    public List<ItineraryResponseDto> getItinerariesByTravelId(Long travelId) {
        List<Itinerary> itineraries = itineraryRepository.findByTravelId(travelId);
        return itineraries.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItineraryResponseDto updateItinerary(Long id, ItineraryRequestDto itineraryRequestDto) {
        Itinerary existingItinerary = itineraryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Itinerary not found with ID: " + id));

        Travel travel = travelRepository.findById(itineraryRequestDto.travelId())
                .orElseThrow(() -> new IllegalArgumentException("Travel not found with ID: " + itineraryRequestDto.travelId()));

        Vendor vendor = null;
        if (itineraryRequestDto.vendorId() != null) {
            vendor = vendorRepository.findById(itineraryRequestDto.vendorId())
                    .orElseThrow(() -> new IllegalArgumentException("Vendor not found with ID: " + itineraryRequestDto.vendorId()));
        }

        BeanUtils.copyProperties(itineraryRequestDto, existingItinerary, "id"); // Exclude ID from copy
        existingItinerary.setTravel(travel);
        existingItinerary.setVendor(vendor);

        Itinerary updatedItinerary = itineraryRepository.save(existingItinerary);
        return convertToDto(updatedItinerary);
    }

    @Override
    @Transactional
    public void deleteItinerary(Long id) {
        if (!itineraryRepository.existsById(id)) {
            throw new IllegalArgumentException("Itinerary not found with ID: " + id);
        }
        itineraryRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return itineraryRepository.existsById(id);
    }

    private ItineraryResponseDto convertToDto(Itinerary itinerary) {
        Long travelId = (itinerary.getTravel() != null) ? itinerary.getTravel().getId() : null;
        Long vendorId = (itinerary.getVendor() != null) ? itinerary.getVendor().getId() : null;
        return new ItineraryResponseDto(
                itinerary.getId(),
                travelId,
                vendorId,
                itinerary.getActivityName(),
                itinerary.getActivityDate(),
                itinerary.getDescription(),
                itinerary.getLocation()
        );
    }
} 