package com.travelcrm.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.travelcrm.dto.ReservationRequestDto;
import com.travelcrm.dto.ReservationResponseDto;
import com.travelcrm.entity.Itinerary;
import com.travelcrm.entity.Reservation;
import com.travelcrm.entity.Travel;
import com.travelcrm.entity.User;
import com.travelcrm.repository.ItineraryRepository;
import com.travelcrm.repository.ReservationRepository;
import com.travelcrm.repository.TravelRepository;
import com.travelcrm.repository.UserRepository;
import com.travelcrm.service.ReservationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItineraryRepository itineraryRepository;

    @Override
    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto reservationRequestDto) {
        Travel travel = travelRepository.findById(reservationRequestDto.travelId())
                .orElseThrow(() -> new IllegalArgumentException("Travel not found with ID: " + reservationRequestDto.travelId()));

        User user = userRepository.findById(reservationRequestDto.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + reservationRequestDto.userId()));

        Itinerary itinerary = null;
        if (reservationRequestDto.itineraryId() != null) {
            itinerary = itineraryRepository.findById(reservationRequestDto.itineraryId())
                    .orElseThrow(() -> new IllegalArgumentException("Itinerary not found with ID: " + reservationRequestDto.itineraryId()));
        }

        Reservation reservation = new Reservation();
        BeanUtils.copyProperties(reservationRequestDto, reservation);
        reservation.setTravel(travel);
        reservation.setUser(user);
        reservation.setItinerary(itinerary);
        reservation.setReservationCode(generateUniqueReservationCode());

        Reservation savedReservation = reservationRepository.save(reservation);
        return convertToDto(savedReservation);
    }

    @Override
    public ReservationResponseDto getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with ID: " + id));
        return convertToDto(reservation);
    }

    @Override
    public ReservationResponseDto getReservationByCode(String reservationCode) {
        Reservation reservation = reservationRepository.findByReservationCode(reservationCode)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with code: " + reservationCode));
        return convertToDto(reservation);
    }

    @Override
    public List<ReservationResponseDto> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReservationResponseDto updateReservation(Long id, ReservationRequestDto reservationRequestDto) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with ID: " + id));

        Travel travel = travelRepository.findById(reservationRequestDto.travelId())
                .orElseThrow(() -> new IllegalArgumentException("Travel not found with ID: " + reservationRequestDto.travelId()));

        User user = userRepository.findById(reservationRequestDto.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + reservationRequestDto.userId()));

        Itinerary itinerary = null;
        if (reservationRequestDto.itineraryId() != null) {
            itinerary = itineraryRepository.findById(reservationRequestDto.itineraryId())
                    .orElseThrow(() -> new IllegalArgumentException("Itinerary not found with ID: " + reservationRequestDto.itineraryId()));
        }

        BeanUtils.copyProperties(reservationRequestDto, existingReservation, "id", "reservationCode"); // Exclude ID and reservationCode from copy
        existingReservation.setTravel(travel);
        existingReservation.setUser(user);
        existingReservation.setItinerary(itinerary);

        Reservation updatedReservation = reservationRepository.save(existingReservation);
        return convertToDto(updatedReservation);
    }

    @Override
    @Transactional
    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new IllegalArgumentException("Reservation not found with ID: " + id);
        }
        reservationRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return reservationRepository.existsById(id);
    }

    @Override
    public String generateQrCodeForReservation(String reservationCode) throws Exception {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            // Assuming a URL structure for public reservation lookup. Adjust as needed.
            String qrContent = "http://localhost:8080/public/reservations/" + reservationCode;
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();
            return Base64.getEncoder().encodeToString(pngData);
        } catch (WriterException | IOException e) {
            throw new Exception("Error generating QR code: " + e.getMessage(), e);
        }
    }

    private String generateUniqueReservationCode() {
        // Generate a UUID and take the first 8 characters for a short code
        String code = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        // Ensure uniqueness - in a real application, you might want more robust checking
        // For simplicity, we assume UUID substring is unique enough for this example
        return code;
    }

    private ReservationResponseDto convertToDto(Reservation reservation) {
        Long travelId = (reservation.getTravel() != null) ? reservation.getTravel().getId() : null;
        Long userId = (reservation.getUser() != null) ? reservation.getUser().getId() : null;
        Long itineraryId = (reservation.getItinerary() != null) ? reservation.getItinerary().getId() : null;

        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getReservationCode(),
                travelId,
                userId,
                itineraryId,
                reservation.getReservationDate(),
                reservation.getStatus(),
                reservation.getPaymentStatus(),
                reservation.getTotalAmount(),
                reservation.getNotes()
        );
    }
}
