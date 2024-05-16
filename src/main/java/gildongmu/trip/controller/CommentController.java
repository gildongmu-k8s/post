package gildongmu.trip.controller;

import gildongmu.trip.dto.request.CommentCreateRequest;
import gildongmu.trip.dto.request.CommentUpdateRequest;
import gildongmu.trip.dto.response.CommentListResponse;
import gildongmu.trip.dto.response.CommentUpdateResponse;
import gildongmu.trip.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}/comments")
    public ResponseEntity<Void> createComment(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
            @PathVariable("postId") Long postId,
            @RequestBody @Valid CommentCreateRequest commentCreateRequest) {
        commentService.createComment(token, commentCreateRequest, postId);
        return ResponseEntity.ok().build();

    }


    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentListResponse>> getComments(
            @PathVariable("postId") Long postId) {
        return ResponseEntity.ok(commentService.findAllComments(postId));
    }


    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentUpdateResponse> updateComment(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentUpdateRequest commentUpdateRequest) {
        return ResponseEntity.ok(
                commentService.updateComment(postId, commentId, token, commentUpdateRequest));

    }


    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId) {


        commentService.deleteComment(postId, commentId, token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}