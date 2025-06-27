package com.travelcrm.service.impl;

import com.travelcrm.dto.ReservationRequestDto;
import com.travelcrm.dto.ReservationResponseDto;
import com.travelcrm.entity.Reservation;
import com.travelcrm.entity.Travel;
import com.travelcrm.entity.User;
import com.travelcrm.repository.ReservationRepository;
import com.travelcrm.repository.TravelRepository;
import com.travelcrm.repository.UserRepository;
import com.travelcrm.service.ReservationService;
import com.travelcrm.util.ApiResponse;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private UserRepository userRepository;

    private ReservationResponseDto convertToDto(Reservation reservation) {
        String travelName = reservation.getTravel() != null ? reservation.getTravel().getName() : null;
        String userName = reservation.getUser() != null ? reservation.getUser().getUsername() : null;
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getTravel().getId(),
                travelName,
                reservation.getUser().getId(),
                userName,
                reservation.getReservationDate(),
                reservation.getStatus(),
                reservation.getNotes(),
                reservation.getQrCodeUrl()
        );
    }

    @Override
    public List<ReservationResponseDto> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationResponseDto getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found with id: " + id));
        return convertToDto(reservation);
    }

    @Override
    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto reservationRequestDto) {
        Travel travel = travelRepository.findById(reservationRequestDto.travelId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Travel not found with id: " + reservationRequestDto.travelId()));
        User user = userRepository.findById(reservationRequestDto.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + reservationRequestDto.userId()));

        Reservation reservation = new Reservation();
        BeanUtils.copyProperties(reservationRequestDto, reservation);
        reservation.setTravel(travel);
        reservation.setUser(user);

        // Generate QR code for the reservation
        try {
            String qrCodeData = "Reservation ID: " + reservation.getId() + ", Travel: " + travel.getName() + ", User: " + user.getUsername();
            String qrCodeUrl = generateQrCodeForReservation(qrCodeData);
            reservation.setQrCodeUrl(qrCodeUrl);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate QR code", e);
        }

        Reservation savedReservation = reservationRepository.save(reservation);
        return convertToDto(savedReservation);
    }

    @Override
    @Transactional
    public ReservationResponseDto updateReservation(Long id, ReservationRequestDto reservationRequestDto) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found with id: " + id));

        Travel travel = travelRepository.findById(reservationRequestDto.travelId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Travel not found with id: " + reservationRequestDto.travelId()));
        User user = userRepository.findById(reservationRequestDto.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + reservationRequestDto.userId()));

        BeanUtils.copyProperties(reservationRequestDto, existingReservation, "id", "qrCodeUrl"); // Exclude id and qrCodeUrl from copying
        existingReservation.setTravel(travel);
        existingReservation.setUser(user);

        // Regenerate QR code if relevant data changes (optional, depending on requirements)
        try {
            String qrCodeData = "Reservation ID: " + existingReservation.getId() + ", Travel: " + travel.getName() + ", User: " + user.getUsername();
            String qrCodeUrl = generateQrCodeForReservation(qrCodeData);
            existingReservation.setQrCodeUrl(qrCodeUrl);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to regenerate QR code", e);
        }

        Reservation updatedReservation = reservationRepository.save(existingReservation);
        return convertToDto(updatedReservation);
    }

    @Override
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found with id: " + id));
        reservationRepository.delete(reservation);
    }

    @Override
    public List<ReservationResponseDto> getReservationsByTravelId(Long travelId) {
        return reservationRepository.findByTravelId(travelId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationResponseDto> getReservationsByUserId(Long userId) {
        return reservationRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private String generateQrCodeForReservation(String data) throws Exception {
        int width = 200;
        int height = 200;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(pngData);
    }

    @Override
    public ReservationResponseDto getReservationByCode(String reservationCode) {
        Reservation reservation = reservationRepository.findByReservationCode(reservationCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found with code: " + reservationCode));
        return convertToDto(reservation);
    }
}
