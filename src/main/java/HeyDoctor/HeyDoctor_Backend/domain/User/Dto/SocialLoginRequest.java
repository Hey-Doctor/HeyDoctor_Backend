package HeyDoctor.HeyDoctor_Backend.domain.User.Dto;

import lombok.Getter;
import lombok.Setter;

// DTO 클래스
@Getter
@Setter
public class SocialLoginRequest {
    private String provider;  // "google", "kakao", "naver"
    private String token;     // SDK에서 받은 토큰 (idToken 또는 accessToken)
}
