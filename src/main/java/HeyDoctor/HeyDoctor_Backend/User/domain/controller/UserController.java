package com.example.signup.controller;

import com.example.signup.dto.*;
import com.example.signup.entity.User;
import com.example.signup.service.UserService;
import com.example.signup.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public CommonResponse<?> register(@RequestBody UserRegisterRequest request) {
        try {
            User user = userService.register(request);
            return new CommonResponse<>(false, user);
        } catch (IllegalStateException e) {
            return new CommonResponse<>(true, e.getMessage());
        } catch (Exception e) {
            return new CommonResponse<>(true, "회원가입 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public CommonResponse<?> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.login(request.getEmail(), request.getPassword());

            if (user == null) {
                return new CommonResponse<>(true, "이메일 또는 비밀번호가 일치하지 않습니다.");
            }

            String token = JwtUtil.generateToken(user.getUsername());
            LoginResponse response = new LoginResponse(user, token);

            return new CommonResponse<>(false, response);

        } catch (Exception e) {
            return new CommonResponse<>(true, "로그인 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    @PutMapping("/update")
    public CommonResponse<?> updateUserInfo(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> updates) {
        try {
            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
            String username = JwtUtil.extractUsername(token);

            User updatedUser = userService.updateUserInfoByMap(username, updates);

            return new CommonResponse<>(false, updatedUser);
        } catch (IllegalStateException e) {
            return new CommonResponse<>(true, e.getMessage());
        } catch (Exception e) {
            return new CommonResponse<>(true, "유저 정보 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
