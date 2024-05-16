package gildongmu.trip.controller;

import gildongmu.trip.dto.response.PostListResponse;
import gildongmu.trip.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final PostService postService;


    @GetMapping
    public ResponseEntity<PostListResponse> searchPosts(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
            @RequestParam(required = false, value = "search") String keyword,
            @RequestParam(required = false, value = "sortby") String postSort,
            @RequestParam(required = false, value = "filter") String postFilter,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {

        Pageable pageableWithoutSort = PageRequest.of(
                pageable.getPageNumber(), pageable.getPageSize(), Sort.unsorted());

        return ResponseEntity.ok(postService.findPosts(keyword, postFilter, postSort, pageableWithoutSort, token));
    }

}
