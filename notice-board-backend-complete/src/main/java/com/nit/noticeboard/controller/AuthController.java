package com.nit.noticeboard.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;

import com.nit.noticeboard.dto.LoginDTO;
import com.nit.noticeboard.dto.LoginResponseDTO;
import com.nit.noticeboard.dto.RegisterDTO;
import com.nit.noticeboard.dto.ResetPasswordDTO;

import com.nit.noticeboard.model.Role;
import com.nit.noticeboard.model.User;

import com.nit.noticeboard.repository.UserRepository;

import com.nit.noticeboard.service.EmailService;
import com.nit.noticeboard.service.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // =========================================================
    // REGISTER
    // =========================================================

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterDTO dto
    ) {

    	String email = dto.getEmail().toLowerCase();

    	if (
    	    !email.endsWith("@nitandhra.ac.in") &&
    	    !email.endsWith("@gmail.com")
    	) {
    	    throw new RuntimeException(
    	        "Only @nitandhra.ac.in or @gmail.com allowed"
    	    );
    	}

        String phone = dto.getPhone()
                .trim();

        // ================= CHECK EMAIL =================

        if (userRepo.existsByEmail(email)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email already registered"
            );
        }

        // ================= CHECK PHONE =================

        if (userRepo.existsByPhone(phone)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Phone number already registered"
            );
        }

        // ================= CREATE USER =================

        User user = new User();

        user.setName(
                dto.getName().trim()
        );

        user.setGender(
                dto.getGender()
        );

        user.setDepartmentName(
                dto.getDepartmentName()
        );

        user.setBranchCode(
                dto.getBranchCode()
        );

        user.setPersonalEmail(
                dto.getPersonalEmail()
        );

        user.setPhone(phone);

        user.setEmail(email);

        // ================= ENCRYPT PASSWORD =================

        String encryptedPassword =
                passwordEncoder.encode(
                        dto.getPassword().trim()
                );

        user.setPassword(encryptedPassword);

        // ================= DEFAULT ROLE =================

        user.setRole(Role.STUDENT);

        // ================= ACTIVE =================

        user.setActive(true);

        // ================= SAVE =================

        userRepo.save(user);
        System.out.println("Registed " + email);


        return ResponseEntity.ok(
                "User registered successfully"
        );
    }

    // =========================================================
    // LOGIN
    // =========================================================

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginDTO req
    ) {


        String email = req.getEmail()
                .trim()
                .toLowerCase();
        System.out.println("LOGIN HIT" + email);

        String rawPassword = req.getPassword()
                .trim();

        // ================= FIND USER =================

        User user = userRepo
                .findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Invalid email"
                        )
                );

        // ================= CHECK ACTIVE =================

        if (!user.isActive()) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Account is deactivated"
            );
        }

        // ================= VERIFY PASSWORD =================

        boolean authenticated =
                passwordEncoder.matches(
                        rawPassword,
                        user.getPassword()
                );

        // ================= INVALID PASSWORD =================

        if (!authenticated) {

            System.out.println("PASSWORD NOT MATCH");

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid password"
            );
        }

        System.out.println("User authenticated successfully");
        
       
   
        // ================= GENERATE JWT =================

        String token = jwtService.generateToken(user);
        System.out.println(user.getRole());

        // ================= RESPONSE =================
        System.out.println("BRANCH = " + user.getBranchCode());

        return ResponseEntity.ok(

                new LoginResponseDTO(
                        token,
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getDepartmentName(),
                        user.getBranchCode(),
                        user.getTeamType()
                )
        );
    }

    // =========================================================
    // FORGOT PASSWORD
    // =========================================================

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestParam String email
    ) {

        email = email.trim().toLowerCase();

        System.out.println(
                "EMAIL RECEIVED: " + email
        );

        User user = userRepo
                .findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Email not found"
                        )
                );

        System.out.println(
                "USER FOUND: " + user.getEmail()
        );

        // ================= GENERATE RESET CODE =================

        String resetCode = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 6)
                .toUpperCase();

        // ================= SAVE TOKEN =================

        user.setResetToken(resetCode);

        // ================= TOKEN EXPIRY =================

        user.setResetTokenExpiry(
                LocalDateTime.now().plusMinutes(5)
        );

        userRepo.save(user);

        // ================= SEND EMAIL =================

        emailService.sendResetToken(
                email,
                resetCode
        );

        return ResponseEntity.ok(
                "Reset code sent to email"
        );
    }

    // =========================================================
    // RESET PASSWORD
    // =========================================================

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestBody ResetPasswordDTO dto
    ) {

        String email = dto.getEmail()
                .trim()
                .toLowerCase();

        String code = dto.getRandomString()
                .trim()
                .toUpperCase();

        User user = userRepo
                .findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Email not found"
                        )
                );

        // ================= CHECK RESET TOKEN =================

        if (
                user.getResetToken() == null
                || !user.getResetToken()
                .equalsIgnoreCase(code)
        ) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid reset code"
            );
        }

        // ================= CHECK EXPIRY =================

        if (
                user.getResetTokenExpiry() == null
                || user.getResetTokenExpiry()
                .isBefore(LocalDateTime.now())
        ) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Reset code expired"
            );
        }

        // ================= ENCRYPT NEW PASSWORD =================

        String encryptedPassword =
                passwordEncoder.encode(
                        dto.getNewPassword().trim()
                );

        // ================= UPDATE PASSWORD =================

        user.setPassword(encryptedPassword);

        // ================= CLEAR RESET TOKEN =================

        user.setResetToken(null);

        // ================= CLEAR EXPIRY =================

        user.setResetTokenExpiry(null);

        // ================= SAVE =================

        userRepo.save(user);

        return ResponseEntity.ok(
                "Password updated successfully"
        );
    }
}
