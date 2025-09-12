package HeyDoctor.HeyDoctor_Backend.global.security.jwt.Token;

import HeyDoctor.HeyDoctor_Backend.global.security.jwt.RefreshToken.entity.RefreshToken;
import HeyDoctor.HeyDoctor_Backend.global.security.jwt.RefreshToken.repository.RefreshTokenRepository;
import HeyDoctor.HeyDoctor_Backend.global.exception.ErrorCode;
import HeyDoctor.HeyDoctor_Backend.global.exception.CustomException;
import HeyDoctor.HeyDoctor_Backend.global.security.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {
    @Value("${jwt.access-token.expiration-time}")
    private long ACCESS_TOKEN_EXPIRATION_TIME;

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    public TokenResponse reissueAccessToken(String authorizationHeader) {
        String refreshToken = jwtUtil.getTokenFromHeader(authorizationHeader);
        String userId = jwtUtil.getUserIdFromToken(refreshToken);

        RefreshToken existRefreshToken = refreshTokenRepository.findByUserId(UUID.fromString(userId));
        if (existRefreshToken == null) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (!existRefreshToken.getToken().equals(refreshToken) || jwtUtil.isTokenExpired(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String accessToken = jwtUtil.generateAccessToken(UUID.fromString(userId), ACCESS_TOKEN_EXPIRATION_TIME);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
