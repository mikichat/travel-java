package com.travelcrm.service;

import com.travelcrm.dto.ReservationRequestDto;
import com.travelcrm.dto.ReservationResponseDto;

import java.util.List;

public interface ReservationService {
    ReservationResponseDto createReservation(ReservationRequestDto reservationRequestDto);
    ReservationResponseDto getReservationById(Long id);
    ReservationResponseDto getReservationByCode(String reservationCode);
    List<ReservationResponseDto> getAllReservations();
    ReservationResponseDto updateReservation(Long id, ReservationRequestDto reservationRequestDto);
    void deleteReservation(Long id);
    boolean existsById(Long id);
    String generateQrCodeForReservation(String reservationCode) throws Exception; // For QR code generation
} 