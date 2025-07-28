package HeyDoctor.HeyDoctor_Backend.global.exception;

import HeyDoctor.HeyDoctor_Backend.global.exception.dto.BaseCode;
import HeyDoctor.HeyDoctor_Backend.global.exception.dto.ResultDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements BaseCode {
    // 전역 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "서버에서 요청을 처리 하는 동안 오류가 발생했습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "400", "입력 값이 잘못된 요청 입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "401", "인증이 필요 합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "403", "금지된 요청 입니다."),

    // 토큰 에러
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "401", "유효하지 않은 토큰입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "401", "유효하지 않은 액세스 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "401", "유효하지 않은 리프레시 토큰입니다."),

    // 유저 에러
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "404", "존재하지 않는 유저입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ResultDto getReason() {
        return ResultDto.builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ResultDto getReasonHttpStatus() {
        return ResultDto.builder()
                .isSuccess(false)
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }
}
