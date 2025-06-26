package com.travelcrm.service;

import com.travelcrm.dto.TravelRequestDto;
import com.travelcrm.dto.TravelResponseDto;

import java.util.List;

public interface TravelService {
    TravelResponseDto createTravel(TravelRequestDto travelRequestDto);
    List<TravelResponseDto> getAllTravels();
    TravelResponseDto getTravelById(Long id);
    TravelResponseDto updateTravel(Long id, TravelRequestDto travelRequestDto);
    void deleteTravel(Long id);
} 