package com.nit.noticeboard.dto;

import com.nit.noticeboard.model.Role;
import com.nit.noticeboard.model.TeamType;

public class LoginResponseDTO {

    // ================= JWT TOKEN =================

    private String token;

    // ================= USER DETAILS =================

    private Long id;

    private String name;

    private String email;

    private Role role;

    private String departmentName;

    private TeamType teamType;

    // ================= CONSTRUCTOR =================

    public LoginResponseDTO(
            String token,
            Long id,
            String name,
            String email,
            Role role,
            String departmentName,
            TeamType teamType
    ) {
        this.token = token;
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.departmentName = departmentName;
        this.teamType = teamType;
    }

    // ================= GETTERS =================

    public String getToken() {
        return token;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public TeamType getTeamType() {
        return teamType;
    }

    // ================= SETTERS =================

    public void setToken(String token) {
        this.token = token;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setTeamType(TeamType teamType) {
        this.teamType = teamType;
    }
}