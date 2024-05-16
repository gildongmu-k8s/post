package gildongmu.trip.dto.response;


import gildongmu.trip.dto.PostItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public record PostListResponse(
    List<PostItem> content,
    Pageable pageable,
    boolean first,
    boolean last,
    int size,
    int number,
    Sort sort,
    int numberOfElements,
    boolean empty,
    int totalPages

) {
}