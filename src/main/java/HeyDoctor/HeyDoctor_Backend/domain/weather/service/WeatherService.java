package HeyDoctor.HeyDoctor_Backend.domain.weather.service;

import HeyDoctor.HeyDoctor_Backend.domain.location.dto.LocationRequestDto;
import HeyDoctor.HeyDoctor_Backend.domain.location.dto.LocationResponseDto;
import HeyDoctor.HeyDoctor_Backend.domain.location.service.LocationService;
import HeyDoctor.HeyDoctor_Backend.domain.weather.dto.OpenWeatherApiResponseDto;
import HeyDoctor.HeyDoctor_Backend.domain.weather.dto.WeatherResponseDto;
import HeyDoctor.HeyDoctor_Backend.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WeatherService {

    private final LocationService locationService;
    private final WebClient webClient;

    @Value("${api.weather.key}")
    private String openWeatherApiKey;

    private static final String OPENWEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    public WeatherService(LocationService locationService, WebClient.Builder webClientBuilder) {
        this.locationService = locationService;
        this.webClient = webClientBuilder.baseUrl(OPENWEATHER_URL).build();
    }

    /**
     * 좌표를 받아서 날씨 정보 반환 (전체 로직을 비동기적으로 처리)
     */
    public Mono<WeatherResponseDto> getWeatherByCoords(LocationRequestDto request) {
        // locationService 호출을 리액티브 체인의 시작점으로 변경
        return locationService.getAdministrativeRegion(request)
                .flatMap(location -> {
                    // location이 null일 경우 BAD_REQUEST 에러 반환
                    if (location == null) {
                        return Mono.error(new RuntimeException(ErrorCode.BAD_REQUEST.getMessage()));
                    }

                    // WebClient를 사용하여 OpenWeatherMap API 호출
                    return webClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .queryParam("lat", request.getLatitude())
                                    .queryParam("lon", request.getLongitude())
                                    .queryParam("appid", openWeatherApiKey)
                                    .queryParam("units", "metric")
                                    .build())
                            .retrieve()
                            .bodyToMono(OpenWeatherApiResponseDto.class)
                            .map(openWeatherResponse -> {
                                // DTO에서 필요한 데이터 추출 및 가공
                                double temp = openWeatherResponse.getMain().getTemp();
                                double feelsLike = openWeatherResponse.getMain().getFeels_like();
                                int humidity = openWeatherResponse.getMain().getHumidity();
                                double windSpeed = openWeatherResponse.getWind().getSpeed();
                                String weatherMain = openWeatherResponse.getWeather().getFirst().getMain();
                                String weatherDescription = openWeatherResponse.getWeather().getFirst().getDescription();

                                double rainVolume = 0;
                                if (openWeatherResponse.getRain() != null) {
                                    rainVolume = openWeatherResponse.getRain().getOneHour();
                                }
                                double snowVolume = 0;
                                if (openWeatherResponse.getSnow() != null) {
                                    snowVolume = openWeatherResponse.getSnow().getOneHour();
                                }

                                // 최종 WeatherResponseDto로 변환하여 반환
                                return WeatherResponseDto.fromOpenWeatherData(
                                        location.getSido(),
                                        location.getSigungu(),
                                        location.getEupmyeondong(),
                                        temp,
                                        feelsLike,
                                        humidity,
                                        windSpeed,
                                        weatherMain,
                                        weatherDescription,
                                        rainVolume,
                                        snowVolume
                                );
                            });
                })
                // locationService에서 빈 Mono가 반환될 경우 에러 처리
                .switchIfEmpty(Mono.error(new RuntimeException(ErrorCode.BAD_REQUEST.getMessage())));
    }
}