package HeyDoctor.HeyDoctor_Backend.domain.DisasterAlert.service;

import HeyDoctor.HeyDoctor_Backend.domain.DisasterAlert.dto.DisasterAlertResponseDto;
import HeyDoctor.HeyDoctor_Backend.domain.location.dto.LocationRequestDto;
import HeyDoctor.HeyDoctor_Backend.domain.location.dto.LocationResponseDto;
import HeyDoctor.HeyDoctor_Backend.domain.location.service.LocationService;
import HeyDoctor.HeyDoctor_Backend.domain.location.util.RegionMatcher;
import HeyDoctor.HeyDoctor_Backend.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class DisasterAlertService {

    private final LocationService locationService;
    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${api.alert.key}")
    private String alertApiKey;

    public DisasterAlertService(LocationService locationService, WebClient.Builder webClientBuilder) {
        this.locationService = locationService;
        this.webClient = webClientBuilder.baseUrl("https://www.safetydata.go.kr/V2/api").build();
    }

    /**
     * 좌표 기반 재난문자 조회 (전체 로직을 비동기적으로 처리)
     */
    public Mono<List<DisasterAlertResponseDto>> getAlertsByCoords(LocationRequestDto request) {
        return locationService.getAdministrativeRegion(request)
                .flatMap(userLocation -> {
                    if (userLocation == null) {
                        return Mono.error(new RuntimeException(ErrorCode.BAD_REQUEST.getMessage()));
                    }
                    return fetchDisasterAlertsFromApi(userLocation)
                            .map(alerts -> {
                                List<DisasterAlertResponseDto> matchedAlerts = new ArrayList<>();
                                for (DisasterAlertResponseDto alert : alerts) {
                                    if (RegionMatcher.matchRegion(alert.getLocation(), userLocation)) {
                                        matchedAlerts.add(alert);
                                    }
                                }
                                return matchedAlerts;
                            });
                })
                // locationService가 빈 Mono를 반환할 경우 에러 처리
                .switchIfEmpty(Mono.error(new RuntimeException(ErrorCode.BAD_REQUEST.getMessage())));
    }

    /**
     * 실제 재난문자 API 호출 후 DTO 변환 (비동기)
     */
    private Mono<List<DisasterAlertResponseDto>> fetchDisasterAlertsFromApi(LocationResponseDto location) {
        String rgnNm = URLEncoder.encode(location.getSido() + " " + location.getSigungu(), StandardCharsets.UTF_8);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/DSSP-IF-00247")
                        .queryParam("serviceKey", alertApiKey)
                        .queryParam("pageNo", "1")
                        .queryParam("numOfRows", "50")
                        .queryParam("returnType", "json")
                        .queryParam("rgnNm", rgnNm)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .handle((rawJson, sink) -> {
                    List<DisasterAlertResponseDto> alerts = new ArrayList<>();
                    try {
                        JsonNode root = objectMapper.readTree(rawJson);
                        JsonNode items = root.path("response").path("body").path("items");
                        if (items.isArray()) {
                            for (JsonNode item : items) {
                                String title = item.path("EMRG_STEP_NM").asText();
                                String message = item.path("MSG_CN").asText();
                                String loc = item.path("RCPTN_RGN_NM").asText();
                                String sendTime = item.path("CRT_DT").asText();
                                alerts.add(DisasterAlertResponseDto.fromApiData(title, message, loc, sendTime));
                            }
                        }
                        sink.next(alerts);
                    } catch (Exception e) {
                        sink.error(new RuntimeException(ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
                    }
                });
    }
}