package HeyDoctor.HeyDoctor_Backend.domain.DisasterAlert.service;

import HeyDoctor.HeyDoctor_Backend.domain.DisasterAlert.dto.DisasterAlertResponseDto;
import HeyDoctor.HeyDoctor_Backend.domain.location.dto.LocationRequestDto;
import HeyDoctor.HeyDoctor_Backend.domain.location.dto.LocationResponseDto;
import HeyDoctor.HeyDoctor_Backend.domain.location.service.LocationService;
import HeyDoctor.HeyDoctor_Backend.domain.location.util.RegionMatcher;
import HeyDoctor.HeyDoctor_Backend.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class DisasterAlertService {

    private static final Logger logger = LoggerFactory.getLogger(DisasterAlertService.class);

    private final LocationService locationService;
    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${api.alert.key}")
    private String alertApiKey;

    public DisasterAlertService(LocationService locationService, WebClient.Builder webClientBuilder) {
        this.locationService = locationService;
        this.webClient = webClientBuilder.baseUrl("https://www.safetydata.go.kr/V2/api").build();
    }

    public Mono<List<DisasterAlertResponseDto>> getAlertsByCoords(LocationRequestDto request) {
        return locationService.getAdministrativeRegion(request)
                .flatMap(userLocation -> {
                    if (userLocation == null) {
                        return Mono.error(new RuntimeException(ErrorCode.BAD_REQUEST.getMessage()));
                    }
                    return fetchDisasterAlertsFromApi(userLocation)
                            .map(alerts -> {
                                // 1. 필터링된 재난문자 리스트 생성
                                List<DisasterAlertResponseDto> matchedAlerts = new ArrayList<>();
                                for (DisasterAlertResponseDto alert : alerts) {
                                    if (RegionMatcher.matchRegion(alert.getLocation(), userLocation)) {
                                        matchedAlerts.add(alert);
                                    }
                                }

                                // 2. 최종 결과를 담을 새로운 리스트 생성
                                List<DisasterAlertResponseDto> finalResponse = new ArrayList<>();

                                // 3. 사용자 위치 정보로 가상의 DTO를 생성
                                String userLocationString = (userLocation.getSido() + " " + userLocation.getSigungu() + " " + userLocation.getEupmyeondong()).trim();
                                DisasterAlertResponseDto locationInfo = new DisasterAlertResponseDto(
                                        "현재 위치",
                                        "요청하신 좌표의 행정구역 정보입니다.",
                                        userLocationString,
                                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
                                );

                                // 4. 최종 리스트의 맨 앞에 위치 정보를 추가
                                finalResponse.add(locationInfo);

                                // 5. 그 뒤에 필터링된 실제 재난문자들을 추가
                                finalResponse.addAll(matchedAlerts);

                                return finalResponse;
                            });
                })
                .switchIfEmpty(Mono.error(new RuntimeException(ErrorCode.BAD_REQUEST.getMessage())));
    }

    private Mono<List<DisasterAlertResponseDto>> fetchDisasterAlertsFromApi(LocationResponseDto location) {
        String rgnNm = URLEncoder.encode(location.getSido() + " " + location.getSigungu(), StandardCharsets.UTF_8);
        String startDate = LocalDate.now().minusDays(7).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/DSSP-IF-00247")
                        .queryParam("serviceKey", alertApiKey)
                        .queryParam("pageNo", "1")
                        .queryParam("numOfRows", "100")
                        .queryParam("returnType", "json")
                        .queryParam("crtDt", startDate)
                        .queryParam("rgnNm", rgnNm)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .handle((rawJson, sink) -> {
                    List<DisasterAlertResponseDto> alerts = new ArrayList<>();
                    try {
                        JsonNode root = objectMapper.readTree(rawJson);
                        JsonNode items = root.path("body");

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
                        logger.error("Failed to parse disaster alert JSON", e);
                        sink.error(new RuntimeException(ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
                    }
                });
    }
}