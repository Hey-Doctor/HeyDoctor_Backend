package HeyDoctor.HeyDoctor_Backend.domain.DisasterAlert.controller;

import HeyDoctor.HeyDoctor_Backend.domain.DisasterAlert.dto.DisasterAlertResponseDto;
import HeyDoctor.HeyDoctor_Backend.domain.DisasterAlert.service.DisasterAlertService;
import HeyDoctor.HeyDoctor_Backend.domain.location.dto.LocationRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/disaster-alert")
public class DisasterAlertController {

    private final DisasterAlertService disasterAlertService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<List<DisasterAlertResponseDto>> getAlerts(
            @RequestParam("lat") double latitude,
            @RequestParam("lon") double longitude) {

        LocationRequestDto request = new LocationRequestDto(latitude, longitude);
        return disasterAlertService.getAlertsByCoords(request);
    }
}