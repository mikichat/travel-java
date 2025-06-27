package com.travelcrm.service;

import com.travelcrm.dto.ReservationRequestDto;
import com.travelcrm.dto.ReservationResponseDto;

import java.util.List;

public interface ReservationService {
    List<ReservationResponseDto> getAllReservations();
    ReservationResponseDto getReservationById(Long id);
    ReservationResponseDto createReservation(ReservationRequestDto reservationRequestDto);
    ReservationResponseDto updateReservation(Long id, ReservationRequestDto reservationRequestDto);
    void deleteReservation(Long id);
    List<ReservationResponseDto> getReservationsByTravelId(Long travelId);
    List<ReservationResponseDto> getReservationsByUserId(Long userId);
    ReservationResponseDto getReservationByCode(String reservationCode);
} 