package gildongmu.trip.service;


import gildongmu.trip.application.UserAdapter;
import gildongmu.trip.domain.comment.entity.Comment;
import gildongmu.trip.domain.comment.repository.CommentRepository;
import gildongmu.trip.domain.post.entity.Post;
import gildongmu.trip.domain.post.repository.PostRepository;
import gildongmu.trip.dto.transfer.UserInfo;
import gildongmu.trip.dto.request.CommentCreateRequest;
import gildongmu.trip.dto.request.CommentUpdateRequest;
import gildongmu.trip.dto.response.CommentListResponse;
import gildongmu.trip.dto.response.CommentUpdateResponse;
import gildongmu.trip.exception.CommentException;
import gildongmu.trip.exception.PostException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gildongmu.trip.exception.ErrorCode.*;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserAdapter userAdapter;

    @Transactional
    public void createComment(String token, CommentCreateRequest commentRequest, Long postId) {
        UserInfo user = userAdapter.getUserInfoFromToken(token);
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(POST_NOT_FOUND));

        Comment parentComment = null;
        if (commentRequest.parentId() != null) {
            parentComment = commentRepository.findById(commentRequest.parentId())
                    .orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND));
        }

        Comment comment = Comment.builder()
                .content(commentRequest.content())
                .secret(commentRequest.secret())
                .userId(user.id())
                .post(post)
                .parent(parentComment)
                .build();

        commentRepository.save(comment);
    }

    public List<CommentListResponse> findAllComments(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(POST_NOT_FOUND));

        List<Comment> comments = commentRepository.findAllByPost(post);
        List<CommentListResponse> commentListResponses = new ArrayList<>();
        Map<Long, CommentListResponse> map = new HashMap<>();

        comments.stream().forEach(comment -> {
            CommentListResponse commentList = mapToCommentListResponse(comment);
            map.put(commentList.id(), commentList);
            if (comment.getParent() != null)
                map.get(comment.getParent().getId()).children().add(commentList);
            else commentListResponses.add(commentList);
        });

        return commentListResponses;
    }

    @Transactional
    public CommentUpdateResponse updateComment(Long postId, Long commentId, String token, CommentUpdateRequest commentRequest) {
        UserInfo user = userAdapter.getUserInfoFromToken(token);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommentException(POST_NOT_FOUND));

        if (!comment.getPost().getId().equals(postId)) {
            throw new CommentException(COMMENT_POST_NOT_FOUND);
        }

        if (!comment.getUserId().equals(user.id())) {
            throw new CommentException(COMMENT_USER_NOT_FOUND);
        }

        comment.updateContent(commentRequest.content());
        comment.updateSecret(commentRequest.secret());

        Comment updatedComment = commentRepository.save(comment);
        updatedComment.addWriter(user);

        return CommentUpdateResponse.from(updatedComment);
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId, String token) {
        UserInfo user = userAdapter.getUserInfoFromToken(token);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommentException(POST_NOT_FOUND));

        if (!comment.getUserId().equals(user.id())) {
            throw new CommentException(COMMENT_USER_NOT_FOUND);
        }

        commentRepository.delete(comment);
    }

    private CommentListResponse mapToCommentListResponse(Comment comment) {
        UserInfo writer = userAdapter.getUserInfoFromId(comment.getUserId());
        comment.addWriter(writer);
        Boolean isOwner = comment.getUserId().equals(comment.getPost().getUserId());

        return new CommentListResponse(
                comment.getId(),
                comment.getWriter().nickname(),
                comment.getWriter().profilePath(),
                comment.getContent(),
                comment.isSecret(),
                isOwner,
                new ArrayList<>()
        );
    }
}
