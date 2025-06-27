package com.travelcrm.service.impl;

import com.travelcrm.dto.UserRequestDto;
import com.travelcrm.dto.UserResponseDto;
import com.travelcrm.entity.User;
import com.travelcrm.repository.UserRepository;
import com.travelcrm.service.UserService;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Tesseract data path. You might want to configure this in application.properties
    private static final String TESSDATA_PATH = "C:/Program Files/Tesseract-OCR/tessdata"; // Adjust for your Tesseract installation

    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        if (userRepository.existsByUsername(userRequestDto.username())) {
            throw new IllegalArgumentException("Username already taken!");
        }
        if (userRepository.existsByEmail(userRequestDto.email())) {
            throw new IllegalArgumentException("Email already in use!");
        }

        User user = new User();
        BeanUtils.copyProperties(userRequestDto, user, "password"); // Copy all except password
        user.setPassword(passwordEncoder.encode(userRequestDto.password())); // Encode password
        user.setRoles(userRequestDto.roles()); // Set roles explicitly

        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        return convertToDto(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));

        // Update only relevant fields from DTO, not username, password, or ID
        BeanUtils.copyProperties(userRequestDto, existingUser, "id", "username", "password");

        // If password is provided in DTO, encode and update
        if (userRequestDto.password() != null && !userRequestDto.password().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(userRequestDto.password()));
        }
        existingUser.setRoles(userRequestDto.roles()); // Update roles

        User updatedUser = userRepository.save(existingUser);
        return convertToDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public UserResponseDto uploadPassportImageAndExtractInfo(Long userId, MultipartFile passportImage) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // Save the image temporarily to a file for Tesseract processing
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "passport_uploads");
        Files.createDirectories(tempDir);
        Path tempFile = tempDir.resolve(passportImage.getOriginalFilename());
        passportImage.transferTo(tempFile.toFile());

        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath(TESSDATA_PATH); // Set the path to tessdata directory
        tesseract.setLanguage("eng"); // Set language to English (or specific passport language)

        String extractedText;
        try {
            BufferedImage bufferedImage = ImageIO.read(tempFile.toFile());
            extractedText = tesseract.doOCR(bufferedImage);
        } catch (TesseractException | IOException e) {
            Files.deleteIfExists(tempFile); // Clean up temp file on error
            throw new Exception("Error processing passport image: " + e.getMessage(), e);
        } finally {
            Files.deleteIfExists(tempFile); // Ensure temp file is deleted
        }

        // Simple regex to extract passport information. This is highly dependent on passport format.
        // A more robust solution would involve a dedicated library or more advanced OCR parsing.
        extractPassportInfo(extractedText, user);

        // For simplicity, store the image URL as a local path or cloud storage URL
        // In a real application, you would store this in cloud storage (e.g., S3, GCS)
        // and save the URL. For now, we'll use a placeholder or local path if saving.
        user.setPassportImageUrl("path/to/saved/passport/image/" + passportImage.getOriginalFilename());

        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }

    private void extractPassportInfo(String extractedText, User user) {
        // Example patterns (highly simplified and may not work for all passports)
        // You will need more robust regex patterns based on actual passport layouts.

        // Passport Number (example: P<GBRABC12345678) -> ABC12345678
        Pattern passportNumPattern = Pattern.compile("P<[A-Z]{3}([A-Z0-9<]{9})", Pattern.CASE_INSENSITIVE);
        Matcher passportNumMatcher = passportNumPattern.matcher(extractedText);
        if (passportNumMatcher.find()) {
            user.setPassportNumber(passportNumMatcher.group(1).replace("<", ""));
        }

        // Date of Birth (format YYMMDD, example: 700101)
        Pattern dobPattern = Pattern.compile("D O B\\s*([0-9]{6})", Pattern.CASE_INSENSITIVE);
        Matcher dobMatcher = dobPattern.matcher(extractedText);
        if (dobMatcher.find()) {
            try {
                // This is a simplified parsing. Consider full date if available.
                String dob = dobMatcher.group(1);
                int year = Integer.parseInt(dob.substring(0, 2));
                int month = Integer.parseInt(dob.substring(2, 4));
                int day = Integer.parseInt(dob.substring(4, 6));
                // Adjust year for 20th vs 21st century
                int fullYear = (year < LocalDate.now().getYear() % 100) ? 2000 + year : 1900 + year;
                user.setDateOfBirth(LocalDate.of(fullYear, month, day));
            } catch (Exception e) {
                // Log error parsing date
            }
        }

        // Issue Date and Expiry Date (format YYMMDD)
        Pattern datePattern = Pattern.compile("([0-9]{6})\s*(EXP|ISS|DATE OF ISSUE|DATE OF EXPIRY)", Pattern.CASE_INSENSITIVE);
        Matcher dateMatcher = datePattern.matcher(extractedText);
        LocalDate issueDate = null;
        LocalDate expiryDate = null;
        while (dateMatcher.find()) {
            try {
                String dateStr = dateMatcher.group(1);
                int year = Integer.parseInt(dateStr.substring(0, 2));
                int month = Integer.parseInt(dateStr.substring(2, 4));
                int day = Integer.parseInt(dateStr.substring(4, 6));
                int fullYear = (year < LocalDate.now().getYear() % 100) ? 2000 + year : 1900 + year;
                LocalDate parsedDate = LocalDate.of(fullYear, month, day);

                String type = dateMatcher.group(2).toUpperCase();
                if (type.contains("ISS")) {
                    issueDate = parsedDate;
                } else if (type.contains("EXP")) {
                    expiryDate = parsedDate;
                }
            } catch (Exception e) {
                // Log error parsing date
            }
        }
        user.setPassportIssueDate(issueDate);
        user.setPassportExpiryDate(expiryDate);

        // First Name, Last Name, Nationality, Gender, Phone Number (more complex, requires careful regex)
        // These are placeholders, real extraction would be more involved.
        // You'll need to analyze typical passport layouts for robust extraction.

        // Example for name (very simplified, usually it's on MRZ or specific lines)
        Pattern namePattern = Pattern.compile("Name:\s*([A-Z<]+)\s*([A-Z<]+)", Pattern.CASE_INSENSITIVE);
        Matcher nameMatcher = namePattern.matcher(extractedText);
        if (nameMatcher.find()) {
            user.setFirstName(nameMatcher.group(2).replace("<", ""));
            user.setLastName(nameMatcher.group(1).replace("<", ""));
        }
        // Nationality (example: GBR)
        Pattern nationalityPattern = Pattern.compile("Nationality:\s*([A-Z]{3})", Pattern.CASE_INSENSITIVE);
        Matcher nationalityMatcher = nationalityPattern.matcher(extractedText);
        if (nationalityMatcher.find()) {
            user.setNationality(nationalityMatcher.group(1));
        }
        // Gender (M/F)
        Pattern genderPattern = Pattern.compile("Gender:\s*([M|F])", Pattern.CASE_INSENSITIVE);
        Matcher genderMatcher = genderPattern.matcher(extractedText);
        if (genderMatcher.find()) {
            user.setGender(genderMatcher.group(1));
        }
        // Phone Number (very general, might need refinement)
        Pattern phonePattern = Pattern.compile("Phone:\s*([+\\d()\\s-]+)", Pattern.CASE_INSENSITIVE);
        Matcher phoneMatcher = phonePattern.matcher(extractedText);
        if (phoneMatcher.find()) {
            user.setPhoneNumber(phoneMatcher.group(1));
        }
    }

    private UserResponseDto convertToDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getDateOfBirth(),
                user.getGender(),
                user.getNationality(),
                user.getPassportNumber(),
                user.getPassportIssueDate(),
                user.getPassportExpiryDate(),
                user.getPassportImageUrl(),
                user.getPhoneNumber(),
                user.getRoles()
        );
    }
} 