package com.travelcrm.controller;

import com.travelcrm.dto.ItineraryRequestDto;
import com.travelcrm.dto.ItineraryResponseDto;
import com.travelcrm.service.ItineraryService;
import com.travelcrm.util.ApiResponse;
import com.travelcrm.util.GlobalExceptionHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itineraries")
public class ItineraryController {

    @Autowired
    private ItineraryService itineraryService;

    @PostMapping
    public ResponseEntity<ApiResponse<ItineraryResponseDto>> createItinerary(@Valid @RequestBody ItineraryRequestDto itineraryRequestDto) {
        try {
            ItineraryResponseDto createdItinerary = itineraryService.createItinerary(itineraryRequestDto);
            return new ResponseEntity<>(ApiResponse.success("Itinerary created successfully", createdItinerary), HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(ApiResponse.error("Error creating itinerary: " + ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ItineraryResponseDto>> getItineraryById(@PathVariable Long id) {
        try {
            ItineraryResponseDto itinerary = itineraryService.getItineraryById(id);
            return new ResponseEntity<>(ApiResponse.success("Itinerary fetched successfully", itinerary), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(ApiResponse.error("Error fetching itinerary: " + ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/travel/{travelId}")
    public ResponseEntity<ApiResponse<List<ItineraryResponseDto>>> getItinerariesByTravelId(@PathVariable Long travelId) {
        try {
            List<ItineraryResponseDto> itineraries = itineraryService.getItinerariesByTravelId(travelId);
            return new ResponseEntity<>(ApiResponse.success("Itineraries fetched successfully", itineraries), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(ApiResponse.error("Error fetching itineraries: " + ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ItineraryResponseDto>> updateItinerary(@PathVariable Long id, @Valid @RequestBody ItineraryRequestDto itineraryRequestDto) {
        try {
            ItineraryResponseDto updatedItinerary = itineraryService.updateItinerary(id, itineraryRequestDto);
            return new ResponseEntity<>(ApiResponse.success("Itinerary updated successfully", updatedItinerary), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(ApiResponse.error("Error updating itinerary: " + ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteItinerary(@PathVariable Long id) {
        try {
            itineraryService.deleteItinerary(id);
            return new ResponseEntity<>(ApiResponse.success("Itinerary deleted successfully", null), HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(ApiResponse.error("Error deleting itinerary: " + ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 