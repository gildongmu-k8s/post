package gildongmu.trip.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostException extends RuntimeException {
    private final ErrorCode errorCode;
}
