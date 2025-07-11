package HeyDoctor.HeyDoctor_Backend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    MEMBER_NOT_FOUND("Member not found");

    private final String message;
}
