package HeyDoctor.HeyDoctor_Backend.global.security.jwt.Token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenResponse {
    @JsonProperty("access_token")
    private String accessToken;
}