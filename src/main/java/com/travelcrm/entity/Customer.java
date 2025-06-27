package com.travelcrm.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private String phone;
    private String address;

    private String passportNumber;
    private LocalDate passportIssueDate;
    private LocalDate passportExpiryDate;
    private String passportCountry;

    @Column(length = 2048) // Store URL or path to scanned passport
    private String passportScanUrl;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
} 