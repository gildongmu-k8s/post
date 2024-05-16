package gildongmu.trip.dto;

import java.util.List;

public record PostItem(
        Long id,
        String title,
        String nickname,
        String destination,
        TripDate tripDate,
        Short numberOfPeople,
        String gender,
        String content,
        String status,
        List<String> tag,
        String thumbnail,
        Long countOfComments,
        Long countOfBookmarks,
        boolean myBookmark
) {
}
