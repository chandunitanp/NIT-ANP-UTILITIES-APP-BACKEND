package com.nit.noticeboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.nit.noticeboard.model.UpdateProfileDTO;
import com.nit.noticeboard.model.User;
import com.nit.noticeboard.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    // =========================================================
    // GET PROFILE
    // =========================================================

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(
            Authentication authentication
    ) {

        String email = authentication.getName();

        User user = userRepo
                .findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        return ResponseEntity.ok(user);
    }

    // =========================================================
    // UPDATE PROFILE
    // =========================================================

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            Authentication authentication,
            @RequestBody UpdateProfileDTO dto
    ) {

        String email = authentication.getName();

        User user = userRepo
                .findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        user.setName(dto.getName().trim());

        user.setGender(dto.getGender());

        user.setPhone(dto.getPhone().trim());

        user.setPersonalEmail(dto.getPersonalEmail().trim());

        userRepo.save(user);

        return ResponseEntity.ok("Profile updated successfully");
    }
}
