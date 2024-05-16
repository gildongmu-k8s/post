package gildongmu.trip.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class S3Exception extends RuntimeException {
    private final ErrorCode errorCode;
}
