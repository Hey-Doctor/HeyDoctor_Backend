package HeyDoctor.HeyDoctor_Backend.domain.location.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationRequestDto {
    private double latitude;
    private double longitude;

    // 좌표를 바로 받을 수 있는 생성자 추가
    public LocationRequestDto(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // 기본 생성자 (롬복 또는 수동)
    public LocationRequestDto() {}
}
