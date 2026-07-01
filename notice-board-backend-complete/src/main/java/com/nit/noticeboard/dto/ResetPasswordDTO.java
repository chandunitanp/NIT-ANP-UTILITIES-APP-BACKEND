package com.nit.noticeboard.dto;

public class ResetPasswordDTO {

    private String email;
    private String randomString;
    private String newPassword;

    // ===== GETTERS =====

    public String getEmail() {
        return email;
    }

    public String getRandomString() {
        return randomString;
    }

    public String getNewPassword() {
        return newPassword;
    }

    // ===== SETTERS =====

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRandomString(String randomString) {
        this.randomString = randomString;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
