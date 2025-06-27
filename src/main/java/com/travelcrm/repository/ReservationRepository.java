package com.travelcrm.repository;

import com.travelcrm.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByReservationCode(String reservationCode);
    List<Reservation> findByTravelId(Long travelId);
    List<Reservation> findByUserId(Long userId);
} 