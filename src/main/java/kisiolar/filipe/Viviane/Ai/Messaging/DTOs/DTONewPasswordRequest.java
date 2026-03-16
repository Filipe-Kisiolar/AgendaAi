package kisiolar.filipe.Viviane.Ai.Messaging.DTOs;

public class DTONewPasswordRequest {

    private Long userId;

    private String emailTo;

    private String token;

    private String passwordResetPath;

    public DTONewPasswordRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPasswordResetPath() {
        return passwordResetPath;
    }

    public void setPasswordResetPath(String passwordResetPath) {
        this.passwordResetPath = passwordResetPath;
    }
}
