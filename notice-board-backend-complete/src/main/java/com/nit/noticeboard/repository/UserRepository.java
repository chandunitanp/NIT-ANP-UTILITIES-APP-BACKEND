package com.nit.noticeboard.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nit.noticeboard.model.Role;
import com.nit.noticeboard.model.TeamType;
import com.nit.noticeboard.model.User;

public interface UserRepository
        extends JpaRepository<User, Long> {

    // =========================================================
    // EMAIL
    // =========================================================

    Optional<User> findByEmail(
            String email
    );

    Optional<User> findByEmailIgnoreCase(
            String email
    );

    boolean existsByEmail(
            String email
    );

    // =========================================================
    // PHONE
    // =========================================================

    Optional<User> findByPhone(
            String phone
    );

    boolean existsByPhone(
            String phone
    );

    // =========================================================
    // ROLE
    // =========================================================

    List<User> findByRole(
            Role role
    );

    // =========================================================
    // ROLE + TEAM
    // =========================================================

    List<User> findByRoleAndTeamType(

            Role role,

            TeamType teamType
    );
}