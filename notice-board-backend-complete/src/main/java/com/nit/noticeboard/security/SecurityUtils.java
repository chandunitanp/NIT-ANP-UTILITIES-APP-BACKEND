package com.nit.noticeboard.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class SecurityUtils {

    // =====================================================
    // GET ROLE
    // =====================================================
    public static String getRole() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) return null;

        return auth.getAuthorities()
                .stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse(null);
    }

    // =====================================================
    // GET USERNAME
    // =====================================================
    public static String getUsername() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) return null;

        return auth.getName();
    }

    // =====================================================
    // GET DEPARTMENT (JWT SAFE - JAVA 8/11 COMPATIBLE)
    // =====================================================
    public static String getDepartment() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) return null;

        Object principal = auth.getPrincipal();

        // ✅ JAVA 8/11 SAFE CASTING (NO PATTERN MATCHING)
        if (principal instanceof Jwt) {

            Jwt jwt = (Jwt) principal;
            return jwt.getClaimAsString("department");
        }

        return null;
    }
}