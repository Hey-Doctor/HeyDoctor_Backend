package HeyDoctor.HeyDoctor_Backend.domain.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WeatherResponseDto {
    private String sido;
    private String sigungu;
    private String eupmyeondong;
    private double temperature;          // 실제 온도
    private double feelsLikeTemperature; // 체감 온도
    private int humidity;                // %
    private String precipitation;        // "없음", "강수", "강설"
    private double windSpeed;            // m/s
    private String weatherDescription;   // 예: "맑음", "흐림", "비"

    /**
     * OpenWeatherMap API JSON 데이터를 기반으로 DTO 생성
     */
    public static WeatherResponseDto fromOpenWeatherData(
            String sido,
            String sigungu,
            String eupmyeondong,
            double temp,
            double feelsLike,
            int humidity,
            double windSpeed,
            String weatherMain,
            String weatherDescription,
            double rainVolume,
            double snowVolume
    ) {
        String precipitationType = "없음";
        if (rainVolume > 0) precipitationType = "강수";
        else if (snowVolume > 0) precipitationType = "강설";

        return new WeatherResponseDto(
                sido,
                sigungu,
                eupmyeondong,
                temp,
                feelsLike,
                humidity,
                precipitationType,
                windSpeed,
                weatherDescription
        );
    }
}
