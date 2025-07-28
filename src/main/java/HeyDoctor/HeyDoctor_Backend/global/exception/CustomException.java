package HeyDoctor.HeyDoctor_Backend.global.exception;

import HeyDoctor.HeyDoctor_Backend.global.exception.dto.ResultDto;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    // 기본 ErrorCode 메시지를 사용하는 생성자
    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    // 커스텀 메시지를 사용하는 생성자
    public CustomException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }
}
