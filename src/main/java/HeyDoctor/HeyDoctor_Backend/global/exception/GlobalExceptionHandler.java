package HeyDoctor.HeyDoctor_Backend.global.exception;

import HeyDoctor.HeyDoctor_Backend.global.exception.ErrorCode;
import HeyDoctor.HeyDoctor_Backend.global.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 사용자 정의 예외 처리
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<?> handleCustomException(CustomException e) {
        ErrorCode code = e.getErrorCode();
        HttpStatus status = code.getHttpStatus();
        log.warn("CustomException occurred: {}", code.getMessage(), e);
        return ResponseEntity
                .status(status)
                .body(Map.of(
                        "success", false,
                        "error", code.getMessage()
                ));
    }
}
