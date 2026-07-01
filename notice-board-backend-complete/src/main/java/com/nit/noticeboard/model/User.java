package com.nit.noticeboard.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({
        "hibernateLazyInitializer",
        "handler"
})
public class User {

    // ================= ID =================

    @Id
    @GeneratedValue(strategy =
            GenerationType.IDENTITY)
    private Long id;

    // ================= BASIC INFO =================

    @Column(nullable = false)
    private String name;

    private String gender;

    private String departmentName;

    private String branchCode;

    private String personalEmail;

    private String phone;

    // ================= LOGIN INFO =================

    @Column(
            unique = true,
            nullable = false
    )
    private String email;

    @Column(nullable = false)
    private String password;

    // ================= ROLE =================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // ================= TEAM TYPE =================

    @Enumerated(EnumType.STRING)
    private TeamType teamType;

    // ================= ACCOUNT STATUS =================

    @Column(nullable = false)
    private boolean active = true;

    // ================= PASSWORD RESET =================

    private String resetToken;

    private LocalDateTime resetTokenExpiry;

    // ================= TIMESTAMPS =================

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // ================= CONSTRUCTOR =================

    public User() {
    }

    // ================= AUTO TIMESTAMPS =================

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }

    // =====================================================
    // ID
    // =====================================================

    public Long getId() {
        return id;
    }

    // =====================================================
    // NAME
    // =====================================================

    public String getName() {
        return name;
    }

    public void setName(
            String name
    ) {
        this.name = name;
    }

    // =====================================================
    // GENDER
    // =====================================================

    public String getGender() {
        return gender;
    }

    public void setGender(
            String gender
    ) {
        this.gender = gender;
    }

    // =====================================================
    // DEPARTMENT NAME
    // =====================================================

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(
            String departmentName
    ) {
        this.departmentName =
                departmentName;
    }

    // =====================================================
    // BRANCH CODE
    // =====================================================

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(
            String branchCode
    ) {
        this.branchCode = branchCode;
    }

    // =====================================================
    // PERSONAL EMAIL
    // =====================================================

    public String getPersonalEmail() {
        return personalEmail;
    }

    public void setPersonalEmail(
            String personalEmail
    ) {
        this.personalEmail =
                personalEmail;
    }

    // =====================================================
    // PHONE
    // =====================================================

    public String getPhone() {
        return phone;
    }

    public void setPhone(
            String phone
    ) {
        this.phone = phone;
    }

    // =====================================================
    // EMAIL
    // =====================================================

    public String getEmail() {
        return email;
    }

    public void setEmail(
            String email
    ) {
        this.email = email;
    }

    // =====================================================
    // PASSWORD
    // =====================================================

    public String getPassword() {
        return password;
    }

    // BCrypt encoded password
    public void setPassword(
            String password
    ) {
        this.password = password;
    }

    // =====================================================
    // ROLE
    // =====================================================

    public Role getRole() {
        return role;
    }

    public void setRole(
            Role role
    ) {
        this.role = role;
    }

    // =====================================================
    // TEAM TYPE
    // =====================================================

    public TeamType getTeamType() {
        return teamType;
    }

    public void setTeamType(
            TeamType teamType
    ) {
        this.teamType = teamType;
    }

    // =====================================================
    // ACTIVE
    // =====================================================

    public boolean isActive() {
        return active;
    }

    public void setActive(
            boolean active
    ) {
        this.active = active;
    }

    // =====================================================
    // RESET TOKEN
    // =====================================================

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(
            String resetToken
    ) {
        this.resetToken = resetToken;
    }

    public LocalDateTime
    getResetTokenExpiry() {

        return resetTokenExpiry;
    }

    public void setResetTokenExpiry(
            LocalDateTime
                    resetTokenExpiry
    ) {

        this.resetTokenExpiry =
                resetTokenExpiry;
    }

    // =====================================================
    // CREATED AT
    // =====================================================

    public LocalDateTime
    getCreatedAt() {

        return createdAt;
    }

    // =====================================================
    // UPDATED AT
    // =====================================================

    public LocalDateTime
    getUpdatedAt() {

        return updatedAt;
    }
}