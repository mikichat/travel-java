package com.travelcrm.controller;

import com.travelcrm.dto.ReservationRequestDto;
import com.travelcrm.dto.ReservationResponseDto;
import com.travelcrm.service.ReservationService;
import com.travelcrm.util.ApiResponse;
import com.travelcrm.util.GlobalExceptionHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReservationResponseDto>> createReservation(@Valid @RequestBody ReservationRequestDto reservationRequestDto) {
        try {
            ReservationResponseDto createdReservation = reservationService.createReservation(reservationRequestDto);
            return new ResponseEntity<>(ApiResponse.success("Reservation created successfully", createdReservation), HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(ApiResponse.error("Error creating reservation: " + ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservationResponseDto>> getReservationById(@PathVariable Long id) {
        try {
            ReservationResponseDto reservation = reservationService.getReservationById(id);
            return new ResponseEntity<>(ApiResponse.success("Reservation fetched successfully", reservation), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(ApiResponse.error("Error fetching reservation: " + ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/code/{reservationCode}")
    public ResponseEntity<ApiResponse<ReservationResponseDto>> getReservationByCode(@PathVariable String reservationCode) {
        try {
            ReservationResponseDto reservation = reservationService.getReservationByCode(reservationCode);
            return new ResponseEntity<>(ApiResponse.success("Reservation fetched successfully by code", reservation), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(ApiResponse.error("Error fetching reservation by code: " + ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReservationResponseDto>>> getAllReservations() {
        try {
            List<ReservationResponseDto> reservations = reservationService.getAllReservations();
            return new ResponseEntity<>(ApiResponse.success("All reservations fetched successfully", reservations), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ApiResponse.error("Error fetching all reservations: " + ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservationResponseDto>> updateReservation(@PathVariable Long id, @Valid @RequestBody ReservationRequestDto reservationRequestDto) {
        try {
            ReservationResponseDto updatedReservation = reservationService.updateReservation(id, reservationRequestDto);
            return new ResponseEntity<>(ApiResponse.success("Reservation updated successfully", updatedReservation), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(ApiResponse.error("Error updating reservation: " + ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteReservation(@PathVariable Long id) {
        try {
            reservationService.deleteReservation(id);
            return new ResponseEntity<>(ApiResponse.success("Reservation deleted successfully", null), HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(ApiResponse.error("Error deleting reservation: " + ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/qrcode/{reservationCode}")
    public ResponseEntity<ApiResponse<String>> getQrCodeForReservation(@PathVariable String reservationCode) {
        try {
            String qrCodeBase64 = reservationService.generateQrCodeForReservation(reservationCode);
            return new ResponseEntity<>(ApiResponse.success("QR code generated successfully", qrCodeBase64), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(ApiResponse.error("Error generating QR code: " + ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Public API for reservation lookup without authentication
    @GetMapping("/public/{reservationCode}")
    public ResponseEntity<ApiResponse<ReservationResponseDto>> getPublicReservationByCode(@PathVariable String reservationCode) {
        try {
            ReservationResponseDto reservation = reservationService.getReservationByCode(reservationCode);
            return new ResponseEntity<>(ApiResponse.success("Public reservation fetched successfully", reservation), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(ApiResponse.error("Error fetching public reservation: " + ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 