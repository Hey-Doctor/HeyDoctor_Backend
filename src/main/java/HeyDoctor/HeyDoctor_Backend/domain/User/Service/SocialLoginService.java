package HeyDoctor.HeyDoctor_Backend.domain.User.Service;

import HeyDoctor.HeyDoctor_Backend.domain.User.oauth.UserInfo.*;
import HeyDoctor.HeyDoctor_Backend.domain.User.entity.User;
import HeyDoctor.HeyDoctor_Backend.domain.User.repository.UserRepository;
import HeyDoctor.HeyDoctor_Backend.global.exception.CustomException;
import HeyDoctor.HeyDoctor_Backend.global.exception.ErrorCode;
import HeyDoctor.HeyDoctor_Backend.global.security.jwt.RefreshToken.entity.RefreshToken;
import HeyDoctor.HeyDoctor_Backend.global.security.jwt.RefreshToken.repository.RefreshTokenRepository;
import HeyDoctor.HeyDoctor_Backend.global.security.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SocialLoginService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.access-token.expiration-time}")
    private long ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${jwt.refresh-token.expiration-time}")
    private long REFRESH_TOKEN_EXPIRATION_TIME;

    public Map<String, Object> processSocialLogin(String provider, String token) {
        OAuth2UserInfo userInfo = verifyTokenAndGetUserInfo(provider, token);

        String providerId = userInfo.getProviderId();
        String name = userInfo.getName();

        User existUser = userRepository.findByProviderId(providerId);
        User user;
        if (existUser == null) {
            user = User.builder()
                    .userId(UUID.randomUUID())
                    .name(name)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(user);
        } else {
            refreshTokenRepository.deleteByUserId(existUser.getUserId());
            user = existUser;
        }

        String refreshToken = jwtUtil.generateRefreshToken(user.getUserId(), REFRESH_TOKEN_EXPIRATION_TIME);
        RefreshToken newRefreshToken = RefreshToken.builder()
                .userId(user.getUserId())
                .token(refreshToken)
                .build();
        refreshTokenRepository.save(newRefreshToken);

        String accessToken = jwtUtil.generateAccessToken(user.getUserId(), ACCESS_TOKEN_EXPIRATION_TIME);

        return Map.of(
                "name", name,
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }

    private OAuth2UserInfo verifyTokenAndGetUserInfo(String provider, String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token); // Authorization: Bearer {token}
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            switch (provider.toLowerCase()) {
                case "google":
                    String googleUrl = "https://www.googleapis.com/oauth2/v3/userinfo";
                    ResponseEntity<Map> googleResponse = restTemplate.exchange(googleUrl, HttpMethod.GET, entity, Map.class);
                    Map<String, Object> googleAttributes = googleResponse.getBody();
                    return new GoogleUserInfo(googleAttributes);

                case "kakao":
                    String kakaoUrl = "https://kapi.kakao.com/v2/user/me";
                    ResponseEntity<Map> kakaoResponse = restTemplate.exchange(kakaoUrl, HttpMethod.GET, entity, Map.class);
                    Map kakaoAttributes = kakaoResponse.getBody();
                    return new KakaoUserInfo(kakaoAttributes);

                case "naver":
                    String naverUrl = "https://openapi.naver.com/v1/nid/me";
                    ResponseEntity<Map> naverResponse = restTemplate.exchange(naverUrl, HttpMethod.GET, entity, Map.class);
                    Map<String, Object> naverBody = naverResponse.getBody();
                    if (naverBody != null && "success".equals(naverBody.get("message"))) {
                        Map<String, Object> naverAttributes = (Map<String, Object>) naverBody.get("response");
                        return new NaverUserInfo(naverAttributes);
                    } else {
                        throw new CustomException(ErrorCode.INVALID_SOCIAL_TOKEN);
                    }

                default:
                    throw new CustomException(ErrorCode.INVALID_SOCIAL_TOKEN);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_SOCIAL_TOKEN);
        }
    }

}
