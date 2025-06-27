package com.travelcrm.service.impl;

import com.travelcrm.dto.CustomerRequestDto;
import com.travelcrm.dto.CustomerResponseDto;
import com.travelcrm.entity.Customer;
import com.travelcrm.repository.CustomerRepository;
import com.travelcrm.service.CustomerService;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    private CustomerResponseDto convertToDto(Customer customer) {
        return new CustomerResponseDto(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getPassportNumber(),
                customer.getPassportIssueDate(),
                customer.getPassportExpiryDate(),
                customer.getPassportCountry(),
                customer.getPassportScanUrl(),
                customer.getNotes()
        );
    }

    @Override
    public List<CustomerResponseDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponseDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id: " + id));
        return convertToDto(customer);
    }

    @Override
    @Transactional
    public CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto) {
        if (customerRepository.findByEmail(customerRequestDto.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered: " + customerRequestDto.email());
        }
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerRequestDto, customer);
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDto(savedCustomer);
    }

    @Override
    @Transactional
    public CustomerResponseDto updateCustomer(Long id, CustomerRequestDto customerRequestDto) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id: " + id));

        Optional<Customer> existingEmail = customerRepository.findByEmail(customerRequestDto.email());
        if (existingEmail.isPresent() && !existingEmail.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered by another customer: " + customerRequestDto.email());
        }

        BeanUtils.copyProperties(customerRequestDto, existingCustomer, "id");
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return convertToDto(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id: " + id));
        customerRepository.delete(customer);
    }

    @Override
    @Transactional
    public CustomerResponseDto processPassportOcr(MultipartFile passportImage) throws Exception {
        if (passportImage.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "여권 이미지가 비어 있습니다.");
        }

        Path tempFile = null;
        try {
            // Save MultipartFile to a temporary file for Tesseract
            tempFile = Files.createTempFile("passport_", ".png");
            passportImage.transferTo(tempFile.toFile());

            ITesseract tesseract = new Tesseract();
            // Set the path to the tessdata directory (where language data is stored)
            // This path needs to be configured based on your Tesseract installation
            tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata"); // Example path for Windows
            tesseract.setLanguage("eng"); // Or "kor+eng" if Korean is also needed

            String extractedText = tesseract.doOCR(tempFile.toFile());

            // Parse extracted text to get passport details
            CustomerRequestDto customerData = parsePassportData(extractedText);

            // For now, create a new customer with the extracted data. 
            // In a real scenario, you'd likely update an existing one or verify.
            return createCustomer(customerData);

        } catch (IOException | TesseractException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "여권 OCR 처리 중 오류가 발생했습니다: " + e.getMessage(), e);
        } finally {
            if (tempFile != null) {
                Files.deleteIfExists(tempFile); // Clean up temp file
            }
        }
    }

    private CustomerRequestDto parsePassportData(String ocrText) {
        String firstName = "";
        String lastName = "";
        String email = "ocr_extracted_email@example.com"; // Placeholder, as email is rarely on passport
        String phone = "";
        String address = "";
        String passportNumber = "";
        LocalDate passportIssueDate = null;
        LocalDate passportExpiryDate = null;
        String passportCountry = "";
        String passportScanUrl = ""; // This would be the URL where the image is stored after upload
        String notes = "OCR Extracted Data:\n" + ocrText.substring(0, Math.min(ocrText.length(), 500)) + "...";

        // --- Regex for Passport Number (Example: P<USA123456789) ---
        // Adjust regex based on actual passport formats you expect
        Pattern passportNumPattern = Pattern.compile("P<[A-Z]{3}([A-Z0-9<]{9})[0-9]{1}([A-Z0-9<]{15})");
        Matcher passportNumMatcher = passportNumPattern.matcher(ocrText);
        if (passportNumMatcher.find()) {
            passportNumber = passportNumMatcher.group(1).replace("<", "");
        }

        // --- Regex for Names (Example: P<USA<SURNAME<<FIRSTNAME<OTHER) ---
        Pattern namePattern = Pattern.compile("P<[A-Z]{3}<([A-Z<]+)<<([A-Z<]+)");
        Matcher nameMatcher = namePattern.matcher(ocrText);
        if (nameMatcher.find()) {
            lastName = nameMatcher.group(1).replace("<", " ").trim();
            firstName = nameMatcher.group(2).replace("<", " ").trim();
        }

        // --- Regex for Dates (Example: YYMMDD format - often near end of MRZ) ---
        // This is a simplified example. Real MRZ parsing is complex.
        Pattern datePattern = Pattern.compile("\\d{6}"); // Matches 6 digits
        Matcher dateMatcher = datePattern.matcher(ocrText);
        
        // Assuming first 6-digit number is birth date, second is expiry date in MRZ
        // This logic needs to be more robust for real-world passports
        int dateCount = 0;
        while (dateMatcher.find() && dateCount < 2) {
            String dateStr = dateMatcher.group(0);
            try {
                // Convert YYMMDD to YYYY-MM-DD. Handle 2-digit year (e.g., 90 -> 1990, 00 -> 2000)
                int year = Integer.parseInt(dateStr.substring(0, 2));
                int currentYearLastTwoDigits = LocalDate.now().getYear() % 100;
                year = (year > currentYearLastTwoDigits + 10) ? 1900 + year : 2000 + year; // Heuristic for century
                
                LocalDate parsedDate = LocalDate.parse(String.format("%04d%s%s", year, dateStr.substring(2, 4), dateStr.substring(4, 6)), java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
                if (dateCount == 0) {
                    // This is a very rough guess, usually birth date comes first in MRZ
                    // For passport MRZ, the first date is typically date of birth, second is expiry.
                    // Since we don't have birthDate in CustomerRequestDto, we'll try to map to issue/expiry.
                    // For simplicity, let's assume the first date is issue and second is expiry if applicable.
                    if (passportIssueDate == null) {
                        passportIssueDate = parsedDate; // Very naive assumption
                    } else if (passportExpiryDate == null) {
                        passportExpiryDate = parsedDate; // Very naive assumption
                    }
                } else if (dateCount == 1) {
                     if (passportExpiryDate == null) {
                        passportExpiryDate = parsedDate; // Very naive assumption
                    }
                }
            } catch (DateTimeParseException e) {
                System.err.println("Error parsing date: " + dateStr + " - " + e.getMessage());
            }
            dateCount++;
        }

        // --- Regex for Passport Country (Example: P<USA) ---
        Pattern countryPattern = Pattern.compile("P<([A-Z]{3})");
        Matcher countryMatcher = countryPattern.matcher(ocrText);
        if (countryMatcher.find()) {
            passportCountry = countryMatcher.group(1);
        }

        return new CustomerRequestDto(
                firstName,
                lastName,
                email,
                phone,
                address,
                passportNumber,
                passportIssueDate,
                passportExpiryDate,
                passportCountry,
                passportScanUrl,
                notes
        );
    }
} 