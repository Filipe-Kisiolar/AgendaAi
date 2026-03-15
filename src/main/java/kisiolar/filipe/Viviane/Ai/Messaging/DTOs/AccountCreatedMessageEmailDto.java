package kisiolar.filipe.Viviane.Ai.Messaging.DTOs;

public class AccountCreatedMessageEmailDto {
    private Long userId;

    private String emailTo;

    private String userName;

    public AccountCreatedMessageEmailDto() {
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}