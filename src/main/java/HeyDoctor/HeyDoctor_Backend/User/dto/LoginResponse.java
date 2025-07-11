package com.example.signup.dto;

import com.example.signup.entity.User;

public class LoginResponse {
    private User user;
    private String jwt;

    public LoginResponse(User user, String jwt) {
        this.user = user;
        this.jwt = jwt;
    }

    // getter, setter
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getJwt() { return jwt; }
    public void setJwt(String jwt) { this.jwt = jwt; }
}
