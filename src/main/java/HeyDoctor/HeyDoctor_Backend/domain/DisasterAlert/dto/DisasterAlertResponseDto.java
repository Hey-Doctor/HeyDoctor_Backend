package HeyDoctor.HeyDoctor_Backend.domain.DisasterAlert.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DisasterAlertResponseDto {
    private String title;       // 재난문자 제목
    private String message;     // 재난문자 내용
    private String location;    // 재난문자 발생 지역
    private String sendTime;    // 발송 시간

    public static DisasterAlertResponseDto fromApiData(
            String title,
            String message,
            String location,
            String sendTime
    ) {
        return new DisasterAlertResponseDto(title, message, location, sendTime);
    }
}