package HeyDoctor.HeyDoctor_Backend.domain.weather.controller;

import HeyDoctor.HeyDoctor_Backend.domain.location.dto.LocationRequestDto;
import HeyDoctor.HeyDoctor_Backend.domain.weather.dto.WeatherResponseDto;
import HeyDoctor.HeyDoctor_Backend.domain.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<WeatherResponseDto> getWeatherByCoords(
            @RequestParam("lat") double latitude,
            @RequestParam("lon") double longitude) {

        LocationRequestDto request = new LocationRequestDto(latitude, longitude);
        return weatherService.getWeatherByCoords(request);
    }
}