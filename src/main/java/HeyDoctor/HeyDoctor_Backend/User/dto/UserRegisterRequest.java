package com.example.signup.dto;

public class UserRegisterRequest {
    private String username;
    private String email;
    private String password;
    private String intro;

    public UserRegisterRequest() {}

    // getter, setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getIntro() { return intro; }
    public void setIntro(String intro) { this.intro = intro; }
}
