package com.example.signup.service;

import com.example.signup.dto.UserRegisterRequest;
import com.example.signup.entity.User;
import com.example.signup.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public User register(UserRegisterRequest request) {
        if (repository.existsByUsername(request.getUsername())) {
            throw new IllegalStateException("이미 존재하는 사용자명입니다.");
        }

        if (repository.existsByEmailAndPassword(request.getEmail(), request.getPassword())) {
            throw new IllegalStateException("이미 존재하는 이메일과 비밀번호 조합입니다.");
        }

        // 생성일자(createdAt)는 자동 생성되므로 포함하지 않음
        User user = new User(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getIntro()
        );

        return repository.save(user);
    }

    @Transactional(readOnly = true)
    public User login(String email, String password) {
        return repository.findByEmailAndPassword(email, password)
                .orElse(null);
    }
    @Transactional
    public User updateUserInfoByMap(String username, Map<String, Object> updates) {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            switch (key) {
                case "username":
                    throw new IllegalArgumentException("username은 수정할 수 없습니다.");
                case "email":
                    user.setEmail((String) value);
                    break;
                case "password":
                    user.setPassword((String) value);
                    break;
                case "intro":
                    user.setIntro((String) value);
                    break;
                default:
                    throw new IllegalArgumentException("수정할 수 없는 필드: " + key);
            }
        }

        return repository.save(user);
    }
}
