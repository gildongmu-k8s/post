package gildongmu.trip.dto.request;

public record CommentCreateRequest(
        String content,
        boolean secret,
        Long parentId
) {

}
