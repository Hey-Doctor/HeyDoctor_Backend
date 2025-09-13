package HeyDoctor.HeyDoctor_Backend.domain.location.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationResponseDto {
    private String sido;
    private String sigungu;
    private String eupmyeondong;

    public LocationResponseDto() {}

    public LocationResponseDto(String sido, String sigungu, String eupmyeondong) {
        this.sido = sido;
        this.sigungu = sigungu;
        this.eupmyeondong = eupmyeondong;
    }


}
