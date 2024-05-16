package gildongmu.trip.dto.response;

import gildongmu.trip.domain.comment.entity.Comment;

public record CommentUpdateResponse(
    Long id,
    String nickname,
    String content,
    boolean secret,
    boolean owner
) {
    public static CommentUpdateResponse from(Comment comment) {
        Boolean isOwner = comment.getUserId().equals(comment.getPost().getUserId());
        return new CommentUpdateResponse(
            comment.getId(),
            comment.getWriter().nickname(),
            comment.getContent(),
            comment.isSecret(),
            isOwner
        );
    }
}
