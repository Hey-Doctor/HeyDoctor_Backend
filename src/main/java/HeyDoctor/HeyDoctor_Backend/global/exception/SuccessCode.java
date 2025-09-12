package HeyDoctor.HeyDoctor_Backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import HeyDoctor.HeyDoctor_Backend.global.exception.dto.*;

@Getter
@AllArgsConstructor
public enum SuccessCode implements BaseCode {
    // 전역 응답 코드
    _OK(HttpStatus.OK, "S200", "요청이 정상 처리되었습니다."),
    _CREATED(HttpStatus.CREATED, "S201", "리소스가 성공적으로 생성되었습니다."),
    _CREATED_ACCESS_TOKEN(HttpStatus.CREATED, "S202", "액세스 토큰이 재발행되었습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "S210", "로그인에 성공했습니다."),
    REGISTER_SUCCESS(HttpStatus.CREATED, "S211", "회원가입에 성공했습니다."),
    JWT_REISSUE_SUCCESS(HttpStatus.OK, "S212", "JWT 토큰 발급에 성공했습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ResultDto getReason() {
        return ResultDto.builder()
                .isSuccess(true)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ResultDto getReasonHttpStatus() {
        return ResultDto.builder()
                .isSuccess(true)
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }
}
