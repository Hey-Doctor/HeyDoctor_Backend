package com.example.signup.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor // ✅ 모든 필드 생성자 자동 생성
public class UserRegisterRequest {
    private String username;
    private String email;
    private String password;
    private String intro;
}
