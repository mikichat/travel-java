package com.travelcrm.service;

import com.travelcrm.dto.ItineraryRequestDto;
import com.travelcrm.dto.ItineraryResponseDto;

import java.util.List;

public interface ItineraryService {
    ItineraryResponseDto createItinerary(ItineraryRequestDto itineraryRequestDto);
    ItineraryResponseDto getItineraryById(Long id);
    List<ItineraryResponseDto> getItinerariesByTravelId(Long travelId);
    ItineraryResponseDto updateItinerary(Long id, ItineraryRequestDto itineraryRequestDto);
    void deleteItinerary(Long id);
    boolean existsById(Long id);
} 