package HeyDoctor.HeyDoctor_Backend.domain.location.service;

import HeyDoctor.HeyDoctor_Backend.domain.location.dto.LocationRequestDto;
import HeyDoctor.HeyDoctor_Backend.domain.location.dto.LocationResponseDto;
import HeyDoctor.HeyDoctor_Backend.global.exception.ErrorCode;
import org.slf4j.Logger; // 로거 import
import org.slf4j.LoggerFactory; // 로거 import
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

    // 1. 로거 필드 추가
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    private final WebClient webClient;

    @Value("${api.kakao.rest-key}")
    private String KAKAO_API_KEY;

    private final String KAKAO_LOCAL_URL = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json";

    public LocationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(KAKAO_LOCAL_URL).build();
    }

    public Mono<LocationResponseDto> getAdministrativeRegion(LocationRequestDto request) {
        String query = String.format("x=%s&y=%s",
                URLEncoder.encode(String.valueOf(request.getLongitude()), StandardCharsets.UTF_8),
                URLEncoder.encode(String.valueOf(request.getLatitude()), StandardCharsets.UTF_8)
        );

        return webClient.get()
                .uri("?" + query)
                .header("Authorization", "KakaoAK " + KAKAO_API_KEY)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> Mono.error(new RuntimeException(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())))
                .bodyToMono(Map.class)
                // 2. bodyToMono 다음에 doOnNext를 사용하여 응답 데이터를 로그로 출력
                .doOnNext(responseMap -> logger.info("Response from Kakao API: {}", responseMap))
                .mapNotNull(response -> {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> documents = (List<Map<String, Object>>) response.get("documents");

                    if (documents == null || documents.isEmpty()) {
                        return null;
                    }

                    Map<String, Object> regionInfo = documents.getFirst();
                    String sido = (String) regionInfo.get("region_1depth_name");
                    String sigungu = (String) regionInfo.get("region_2depth_name");
                    String eupmyeondong = (String) regionInfo.get("region_3depth_name");

                    return new LocationResponseDto(sido, sigungu, eupmyeondong);
                });
    }
}