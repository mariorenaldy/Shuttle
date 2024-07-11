package com.example.pppb51_tubes02_b_f;

public class LoginResult {
    private boolean success;
    private String token;
    private String userId; // Add this field
    private String error; // Add this field

    // Constructor to set the fields
    public LoginResult(boolean success, String token, String userId, String error) {
        this.success = success;
        this.token = token;
        this.userId = userId;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId; // Add this method
    }

    public String getError() {
        return error; // Add this method
    }
}


