package gildongmu.trip.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // global
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "인증에 실패하였습니다."),
    REQUEST_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "요청 파라미터를 확인해주세요."),
    // s3
    FILE_CONVERT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 변환에 실패하였습니다."),
    WRONG_FILE_FORMAT(HttpStatus.BAD_REQUEST, "잘못된 형식의 확장자입니다."),
    UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "업로드에 실패하였습니다."),
    DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "삭제에 실패하였습니다."),
    // post
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글이 없습니다."),
    // bookmark
    ALREADY_BOOKMARK(HttpStatus.BAD_REQUEST, "이미 찜한 게시물 입니다."),
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "찜한 게시물이 아닙니다."),
    // comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글이 없습니다."),
    COMMENT_USER_NOT_FOUND(HttpStatus.FORBIDDEN, "해당 댓글을 쓴 유저가 아닙니다."),
    COMMENT_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글의 게시글이 아닙니다."),
    ;
    private final HttpStatus httpStatus;
    private final String message;
}

