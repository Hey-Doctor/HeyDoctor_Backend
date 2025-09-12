package HeyDoctor.HeyDoctor_Backend.domain.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

// OpenWeather API 응답의 최상위 객체
@Getter
@Setter
@NoArgsConstructor
public class OpenWeatherApiResponseDto {
    private Main main;
    private Wind wind;
    private List<Weather> weather;
    private Rain rain;
    private Snow snow;

    @Getter
    @Setter
    public static class Main {
        private double temp;
        private double feels_like;
        private int humidity;
    }

    @Getter
    @Setter
    public static class Wind {
        private double speed;
    }

    @Getter
    @Setter
    public static class Weather {
        private String main;
        private String description;
    }

    @Getter
    @Setter
    public static class Rain {
        @JsonProperty("1h")
        private double oneHour;
    }

    @Getter
    @Setter
    public static class Snow {
        @JsonProperty("1h")
        private double oneHour;
    }
}