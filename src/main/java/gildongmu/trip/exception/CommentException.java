package gildongmu.trip.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentException extends RuntimeException{
    private final ErrorCode errorCode;
}
