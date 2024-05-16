package gildongmu.trip.controller;

import gildongmu.trip.dto.PostItem;
import gildongmu.trip.dto.request.PostCreateRequest;
import gildongmu.trip.dto.request.PostUpdateRequest;
import gildongmu.trip.dto.request.RetrievingType;
import gildongmu.trip.dto.response.PostListResponse;
import gildongmu.trip.dto.response.PostResponse;
import gildongmu.trip.dto.response.PostSummaryResponse;
import gildongmu.trip.service.PostService;
import gildongmu.trip.util.validator.EnumValue;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<PostListResponse> getPosts(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @RequestParam(required = false, value = "sortby") String postSort,
            @RequestParam(required = false, value = "filter") String postFilter,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {

        Pageable pageableWithoutSort = PageRequest.of(
            pageable.getPageNumber(), pageable.getPageSize(), Sort.unsorted());

        PostListResponse posts = postService.findPosts(null, postFilter, postSort, pageableWithoutSort, token);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(postService.findPost(postId));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createPost(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token,
            @RequestPart(required = false, value = "images") List<MultipartFile> images,
            @RequestPart @Valid PostCreateRequest postCreateRequest) {

        postService.createPost(postCreateRequest, images, token);
        return ResponseEntity.ok().build();
    }


    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponse> updatePost(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
            @RequestPart(required = false, value = "images") List<MultipartFile> images,
            @PathVariable("postId") Long postId,
            @RequestPart @Valid PostUpdateRequest postUpdateRequest) {

        return ResponseEntity.ok(postService.updatePost(postId, images, postUpdateRequest));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
            @PathVariable Long postId) {

        postService.deletePost(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/me")
    public ResponseEntity<Slice<PostItem>> retrieveMyPosts(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam @EnumValue(enumClass = RetrievingType.class) String type,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
        return ResponseEntity.ok(postService.retrieveMyPosts(token, type, pageable));
    }

    @GetMapping("/{postId}/summary")
    public ResponseEntity<PostSummaryResponse> retrievePostSummary(@PathVariable Long postId,
                                                                   @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
        return ResponseEntity.ok(postService.retrievePostSummary(token, postId));
    }

}
