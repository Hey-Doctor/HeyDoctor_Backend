package HeyDoctor.HeyDoctor_Backend.global.exception.responce;

import HeyDoctor.HeyDoctor_Backend.global.exception.dto.ResultDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;

    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T payload;

    public static <T> ResponseEntity<ApiResponse<T>> onSuccess(ResultDto result, T data) {
        ApiResponse<T> response = new ApiResponse<>(
                true,
                result.getCode(),
                result.getMessage(),
                data
        );
        return ResponseEntity.status(result.getHttpStatus()).body(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> onFailure(ResultDto result) {
        ApiResponse<T> response = new ApiResponse<>(
                false,
                result.getCode(),
                result.getMessage(),
                null
        );
        return ResponseEntity.status(result.getHttpStatus()).body(response);
    }
}
