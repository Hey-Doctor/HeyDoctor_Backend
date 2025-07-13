package com.example.signup.service;

import com.example.signup.dto.UserRegisterRequest;
import com.example.signup.entity.User;
import com.example.signup.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository repository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        repository = mock(UserRepository.class);
        userService = new UserService(repository);
    }

    @Nested
    @DisplayName("회원가입 테스트")
    class RegisterTest {

        @Test
        @DisplayName("성공")
        void register_success() {
            UserRegisterRequest request = new UserRegisterRequest("user1", "user@example.com", "pass", "hello");

            when(repository.existsByUsername("user1")).thenReturn(false);
            when(repository.existsByEmailAndPassword("user@example.com", "pass")).thenReturn(false);
            when(repository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

            User user = userService.register(request);

            System.out.println("✅ 회원가입 성공 - username: " + user.getUsername());

            assertThat(user.getUsername()).isEqualTo("user1");
        }

        @Test
        @DisplayName("이미 존재하는 사용자명")
        void register_duplicateUsername() {
            UserRegisterRequest request = new UserRegisterRequest("user1", "user@example.com", "pass", "hello");
            when(repository.existsByUsername("user1")).thenReturn(true);

            System.out.println("❌ 사용자명 중복 발생 테스트 시작");

            assertThatThrownBy(() -> userService.register(request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("이미 존재하는 사용자명입니다.")
                    .satisfies(e -> {
                        System.out.println("▶ 예외 메시지: " + e.getMessage());
                        System.out.println("✅ 예외가 정상적으로 처리되었습니다.");
                    });
        }

        @Test
        @DisplayName("이미 존재하는 이메일+비밀번호")
        void register_duplicateEmailAndPassword() {
            UserRegisterRequest request = new UserRegisterRequest("user1", "user@example.com", "pass", "hello");

            when(repository.existsByUsername("user1")).thenReturn(false);
            when(repository.existsByEmailAndPassword("user@example.com", "pass")).thenReturn(true);

            System.out.println("❌ 이메일+비밀번호 중복 발생 테스트 시작");

            assertThatThrownBy(() -> userService.register(request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("이미 존재하는 이메일과 비밀번호 조합입니다.")
                    .satisfies(e -> {
                        System.out.println("▶ 예외 메시지: " + e.getMessage());
                        System.out.println("✅ 예외가 정상적으로 처리되었습니다.");
                    });
        }
    }

    @Nested
    @DisplayName("로그인 테스트")
    class LoginTest {

        @Test
        @DisplayName("성공")
        void login_success() {
            User user = new User("user1", "user@example.com", "pass", "hi");

            when(repository.findByEmailAndPassword("user@example.com", "pass"))
                    .thenReturn(Optional.of(user));

            User result = userService.login("user@example.com", "pass");

            System.out.println("✅ 로그인 성공 - username: " + result.getUsername());

            assertThat(result).isNotNull();
            assertThat(result.getUsername()).isEqualTo("user1");
        }

        @Test
        @DisplayName("이메일/비밀번호 불일치")
        void login_invalid() {
            when(repository.findByEmailAndPassword("user@example.com", "wrongpass"))
                    .thenReturn(Optional.empty());

            User result = userService.login("user@example.com", "wrongpass");

            System.out.println("❌ 로그인 실패 - 유저 없음 (결과가 null)");

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("유저 정보 수정 테스트")
    class UpdateUserInfoTest {

        @Test
        @DisplayName("성공")
        void update_success() {
            User user = new User("user1", "user@example.com", "pass", "hi");

            when(repository.findByUsername("user1")).thenReturn(Optional.of(user));
            when(repository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

            Map<String, Object> updates = Map.of("email", "new@example.com", "intro", "updated intro");

            User result = userService.updateUserInfoByMap("user1", updates);

            System.out.println("✅ 유저 정보 수정 성공 - email: " + result.getEmail() + ", intro: " + result.getIntro());

            assertThat(result.getEmail()).isEqualTo("new@example.com");
            assertThat(result.getIntro()).isEqualTo("updated intro");
        }

        @Test
        @DisplayName("username 수정 시도")
        void update_usernameChange() {
            User user = new User("user1", "user@example.com", "pass", "hi");
            when(repository.findByUsername("user1")).thenReturn(Optional.of(user));

            Map<String, Object> updates = Map.of("username", "newUser");

            System.out.println("❌ username 수정 시도 테스트 시작");

            assertThatThrownBy(() -> userService.updateUserInfoByMap("user1", updates))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("username은 수정할 수 없습니다.")
                    .satisfies(e -> {
                        System.out.println("▶ 예외 메시지: " + e.getMessage());
                        System.out.println("✅ 예외가 정상적으로 처리되었습니다.");
                    });
        }

        @Test
        @DisplayName("허용되지 않은 필드 수정 시도")
        void update_invalidField() {
            User user = new User("user1", "user@example.com", "pass", "hi");
            when(repository.findByUsername("user1")).thenReturn(Optional.of(user));

            Map<String, Object> updates = Map.of("role", "admin");

            System.out.println("❌ 허용되지 않은 필드 수정 테스트 시작");

            assertThatThrownBy(() -> userService.updateUserInfoByMap("user1", updates))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("수정할 수 없는 필드: role")
                    .satisfies(e -> {
                        System.out.println("▶ 예외 메시지: " + e.getMessage());
                        System.out.println("✅ 예외가 정상적으로 처리되었습니다.");
                    });
        }

        @Test
        @DisplayName("사용자 없음")
        void update_userNotFound() {
            when(repository.findByUsername("unknown")).thenReturn(Optional.empty());

            Map<String, Object> updates = Map.of("intro", "intro");

            System.out.println("❌ 사용자 없음 테스트 시작");

            assertThatThrownBy(() -> userService.updateUserInfoByMap("unknown", updates))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("사용자를 찾을 수 없습니다.")
                    .satisfies(e -> {
                        System.out.println("▶ 예외 메시지: " + e.getMessage());
                        System.out.println("✅ 예외가 정상적으로 처리되었습니다.");
                    });
        }
    }
}
