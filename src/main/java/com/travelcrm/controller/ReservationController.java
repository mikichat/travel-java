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
            return new ResponseEntity<>(new ApiResponse<>("SUCCESS", "예약을 성공적으로 생성했습니다.", createdReservation), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>("ERROR", "예약 생성 중 오류가 발생했습니다: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservationResponseDto>> getReservationById(@PathVariable Long id) {
        try {
            ReservationResponseDto reservation = reservationService.getReservationById(id);
            return new ResponseEntity<>(new ApiResponse<>("SUCCESS", "예약을 성공적으로 불러왔습니다.", reservation), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>("ERROR", "예약을 불러오는 중 오류가 발생했습니다: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
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
            return new ResponseEntity<>(new ApiResponse<>("SUCCESS", "예약 목록을 성공적으로 불러왔습니다.", reservations), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>("ERROR", "예약 목록을 불러오는 중 오류가 발생했습니다: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservationResponseDto>> updateReservation(@PathVariable Long id, @Valid @RequestBody ReservationRequestDto reservationRequestDto) {
        try {
            ReservationResponseDto updatedReservation = reservationService.updateReservation(id, reservationRequestDto);
            return new ResponseEntity<>(new ApiResponse<>("SUCCESS", "예약을 성공적으로 업데이트했습니다.", updatedReservation), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>("ERROR", "예약 업데이트 중 오류가 발생했습니다: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReservation(@PathVariable Long id) {
        try {
            reservationService.deleteReservation(id);
            return new ResponseEntity<>(new ApiResponse<>("SUCCESS", "예약을 성공적으로 삭제했습니다.", null), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>("ERROR", "예약 삭제 중 오류가 발생했습니다: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Public API for reservation lookup without authentication
    @GetMapping("/public/{reservationCode}")
    public ResponseEntity<ApiResponse<ReservationResponseDto>> getPublicReservationByCode(@PathVariable String reservationCode) {
        try {
            ReservationResponseDto reservation = reservationService.getReservationByCode(reservationCode);
            return new ResponseEntity<>(new ApiResponse<>("SUCCESS", "Public reservation fetched successfully", reservation), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(new ApiResponse<>("ERROR", ex.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse<>("ERROR", "Error fetching public reservation: " + ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/travel/{travelId}")
    public ResponseEntity<ApiResponse<List<ReservationResponseDto>>> getReservationsByTravelId(@PathVariable Long travelId) {
        try {
            List<ReservationResponseDto> reservations = reservationService.getReservationsByTravelId(travelId);
            return new ResponseEntity<>(new ApiResponse<>("SUCCESS", "여행 ID로 예약 목록을 성공적으로 불러왔습니다.", reservations), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>("ERROR", "여행 ID로 예약 목록을 불러오는 중 오류가 발생했습니다: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ReservationResponseDto>>> getReservationsByUserId(@PathVariable Long userId) {
        try {
            List<ReservationResponseDto> reservations = reservationService.getReservationsByUserId(userId);
            return new ResponseEntity<>(new ApiResponse<>("SUCCESS", "사용자 ID로 예약 목록을 성공적으로 불러왔습니다.", reservations), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>("ERROR", "사용자 ID로 예약 목록을 불러오는 중 오류가 발생했습니다: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 