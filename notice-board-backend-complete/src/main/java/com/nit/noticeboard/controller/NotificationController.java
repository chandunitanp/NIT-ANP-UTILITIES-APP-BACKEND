package com.nit.noticeboard.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import com.nit.noticeboard.repository.NotificationRepository;
import com.nit.noticeboard.model.Department;
import com.nit.noticeboard.model.Notification;

import java.nio.file.*;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepository repo;

    private final Path uploadDir = Paths.get("uploads");

    // =====================================================
    // VIEW NOTIFICATIONS
    // =====================================================
    @GetMapping
    public ResponseEntity<?> all(Authentication authentication) {

        String role = authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse(null);

        String dept = null;

        Object principal = authentication.getPrincipal();

        // ✅ Java 8/11 SAFE SYNTAX
        if (principal instanceof org.springframework.security.oauth2.jwt.Jwt) {
            org.springframework.security.oauth2.jwt.Jwt jwt =
                    (org.springframework.security.oauth2.jwt.Jwt) principal;

            dept = jwt.getClaimAsString("department");
        }

        List<Notification> notifications;

        if ("ADMIN".equals(role)) {
            notifications = repo.findAll();
        } else {

            if (dept == null) {
                return ResponseEntity.badRequest()
                        .body("Department missing in token");
            }

            notifications = repo.findByDepartment(
                    Department.fromString(dept)
            );
        }

        return ResponseEntity.ok(notifications);
    }

    // =====================================================
    // CREATE NOTIFICATION (WITH FILE)
    // =====================================================
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    public ResponseEntity<?> upload(
            @RequestParam String title,
            @RequestParam String message,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) MultipartFile file,
            Authentication authentication
    ) {

        try {

            String role = authentication.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(a -> a.getAuthority().replace("ROLE_", ""))
                    .orElse(null);

            String dept = null;

            Object principal = authentication.getPrincipal();

            // ✅ SAFE JWT extraction
            if (principal instanceof org.springframework.security.oauth2.jwt.Jwt) {
                org.springframework.security.oauth2.jwt.Jwt jwt =
                        (org.springframework.security.oauth2.jwt.Jwt) principal;

                dept = jwt.getClaimAsString("department");
            }

            Notification n = new Notification();
            n.setTitle(title);
            n.setMessage(message);

            // =================================================
            // ROLE BASED DEPARTMENT LOGIC
            // =================================================
            if ("FACULTY".equals(role)) {

                if (dept == null) {
                    return ResponseEntity.badRequest()
                            .body("Faculty department missing in token");
                }

                n.setDepartment(Department.fromString(dept));

            } else if ("ADMIN".equals(role)) {

                if (department == null || department.isEmpty()) {
                    return ResponseEntity.badRequest()
                            .body("Department is required for admin");
                }

                n.setDepartment(Department.fromString(department));
            }

            n.setCreatedBy(authentication.getName());

            // =================================================
            // FILE UPLOAD
            // =================================================
            Files.createDirectories(uploadDir);

            if (file != null && !file.isEmpty()) {

                String fileName =
                        System.currentTimeMillis() + "_" + file.getOriginalFilename();

                Files.copy(
                        file.getInputStream(),
                        uploadDir.resolve(fileName),
                        StandardCopyOption.REPLACE_EXISTING
                );

                n.setFilePath(fileName);
            }

            return ResponseEntity.ok(repo.save(n));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Upload failed: " + e.getMessage());
        }
    }

    // =====================================================
    // UPDATE (ADMIN ONLY)
    // =====================================================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody Notification updated
    ) {

        return repo.findById(id)
                .map(existing -> {

                    existing.setTitle(updated.getTitle());
                    existing.setMessage(updated.getMessage());
                    existing.setDepartment(updated.getDepartment());

                    return ResponseEntity.ok(repo.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // =====================================================
    // DELETE (ADMIN ONLY)
    // =====================================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        return repo.findById(id)
                .map(existing -> {
                    repo.delete(existing);
                    return ResponseEntity.ok("Deleted successfully");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Notification not found"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    public ResponseEntity<?> getById(
            @PathVariable Long id
    ) {

        Notification notice =
                repo.findById(id)
                        .orElse(null);

        if (notice == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Notification not found");
        }

        return ResponseEntity.ok(notice);
    }
    
    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
    public ResponseEntity<?> getNotificationCount(Authentication authentication) {

        try {
            String role = authentication.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(a -> a.getAuthority().replace("ROLE_", ""))
                    .orElse(null);

            String dept = null;

            Object principal = authentication.getPrincipal();

            if (principal instanceof org.springframework.security.oauth2.jwt.Jwt) {
                org.springframework.security.oauth2.jwt.Jwt jwt =
                        (org.springframework.security.oauth2.jwt.Jwt) principal;

                dept = jwt.getClaimAsString("department");
            }

            long count;

            if ("ADMIN".equals(role)) {
                count = repo.count(); // admin sees all notifications count
            } else {

                if (dept == null) {
                    return ResponseEntity.badRequest()
                            .body("Department missing in token");
                }

                count = repo.countByDepartment(
                        Department.fromString(dept)
                );
            }

            return ResponseEntity.ok(count);

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Failed to get count: " + e.getMessage());
        }
    }
}