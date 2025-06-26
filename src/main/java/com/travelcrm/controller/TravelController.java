package com.travelcrm.controller;

import com.travelcrm.dto.TravelRequestDto;
import com.travelcrm.dto.TravelResponseDto;
import com.travelcrm.service.TravelService;
import com.travelcrm.util.ApiResponse;
import com.travelcrm.util.GlobalExceptionHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/travels")
public class TravelController {

    @Autowired
    private TravelService travelService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createTravel(@Valid @RequestBody TravelRequestDto travelRequestDto) {
        try {
            TravelResponseDto createdTravel = travelService.createTravel(travelRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Travel created successfully", createdTravel));
        } catch (IllegalArgumentException ex) {
            return GlobalExceptionHandler.errorResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return GlobalExceptionHandler.errorResponseEntity("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllTravels() {
        try {
            List<TravelResponseDto> travels = travelService.getAllTravels();
            return ResponseEntity.ok(ApiResponse.success("Travels retrieved successfully", travels));
        } catch (Exception ex) {
            return GlobalExceptionHandler.errorResponseEntity("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getTravelById(@PathVariable Long id) {
        try {
            TravelResponseDto travel = travelService.getTravelById(id);
            return ResponseEntity.ok(ApiResponse.success("Travel retrieved successfully", travel));
        } catch (IllegalArgumentException ex) {
            return GlobalExceptionHandler.errorResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return GlobalExceptionHandler.errorResponseEntity("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateTravel(@PathVariable Long id, @Valid @RequestBody TravelRequestDto travelRequestDto) {
        try {
            TravelResponseDto updatedTravel = travelService.updateTravel(id, travelRequestDto);
            return ResponseEntity.ok(ApiResponse.success("Travel updated successfully", updatedTravel));
        } catch (IllegalArgumentException ex) {
            return GlobalExceptionHandler.errorResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return GlobalExceptionHandler.errorResponseEntity("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteTravel(@PathVariable Long id) {
        try {
            travelService.deleteTravel(id);
            return ResponseEntity.ok(ApiResponse.success("Travel deleted successfully", null));
        } catch (IllegalArgumentException ex) {
            return GlobalExceptionHandler.errorResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return GlobalExceptionHandler.errorResponseEntity("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 