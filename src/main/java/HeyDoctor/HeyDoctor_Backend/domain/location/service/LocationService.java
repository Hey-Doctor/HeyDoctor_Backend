package HeyDoctor.HeyDoctor_Backend.domain.location.service;

import HeyDoctor.HeyDoctor_Backend.domain.location.dto.LocationRequestDto;
import HeyDoctor.HeyDoctor_Backend.domain.location.dto.LocationResponseDto;
import HeyDoctor.HeyDoctor_Backend.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.List;

@Service
public class LocationService {

    private final WebClient webClient;

    @Value("${api.kakao.rest-key}")
    private String KAKAO_API_KEY;

    private final String KAKAO_LOCAL_URL = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json";

    public LocationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(KAKAO_LOCAL_URL).build();
    }

    /**
     * 좌표를 받아서 행정구역 정보 반환 (비동기, 논블로킹)
     */
    public Mono<LocationResponseDto> getAdministrativeRegion(LocationRequestDto request) {
        String query = String.format("x=%s&y=%s",
                URLEncoder.encode(String.valueOf(request.getLongitude()), StandardCharsets.UTF_8),
                URLEncoder.encode(String.valueOf(request.getLatitude()), StandardCharsets.UTF_8)
        );

        return webClient.get()
                .uri("?" + query)
                .header("Authorization", "KakaoAK " + KAKAO_API_KEY)
                .retrieve()
                // API 응답 코드가 200이 아닌 경우 에러 처리
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> Mono.error(new RuntimeException(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())))
                .bodyToMono(Map.class)
                .map(response -> {
                    // JSON 응답에서 문서(documents) 목록을 추출
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> documents = (List<Map<String, Object>>) response.get("documents");

                    if (documents.isEmpty()) {
                        return null;
                    }

                    // 첫 번째 문서에서 행정구역 정보 추출
                    Map<String, Object> regionInfo = documents.get(0);
                    String sido = (String) regionInfo.get("region_1depth_name");
                    String sigungu = (String) regionInfo.get("region_2depth_name");
                    String eupmyeondong = (String) regionInfo.get("region_3depth_name");

                    return new LocationResponseDto(sido, sigungu, eupmyeondong);
                });
    }
}