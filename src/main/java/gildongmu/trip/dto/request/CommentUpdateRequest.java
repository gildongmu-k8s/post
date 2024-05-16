package gildongmu.trip.dto.request;

public record CommentUpdateRequest(
        String content,
        boolean secret
) {

}
