package com.travelcrm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String reservationCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id", nullable = false)
    private Travel travel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itinerary_id")
    private Itinerary itinerary;

    @Column(nullable = false)
    private LocalDate reservationDate;

    @Column(nullable = false)
    private String status; // Reservation status (e.g., "예약 요청", "계약 확정")

    @Column(nullable = false)
    private String paymentStatus; // Payment status (e.g., "PENDING", "COMPLETED")

    @Column(nullable = false)
    private Double totalAmount;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private String qrCodeUrl;
} 