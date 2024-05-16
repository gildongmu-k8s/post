package gildongmu.trip.dto.response;


import gildongmu.trip.domain.post.entity.Post;
import gildongmu.trip.dto.UserInfo;
import lombok.Builder;

import java.util.Objects;

@Builder
public record PostSummaryResponse(
        Long id,
        String title,
        String content,
        String status,
        int numberOfCapacity,
        int numberOfAccepted,
        User user
) {

    public static PostSummaryResponse from(Post post, int numberOfAccepted, Long currentUserId) {
        return PostSummaryResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .status(post.getStatus().name())
                .content(post.getContent())
                .numberOfAccepted(numberOfAccepted)
                .numberOfCapacity(post.getParticipants())
                .user(User.from(post.getWriter(), currentUserId))
                .build();
    }

    @Builder
    public record User(
            Long id,
            String nickname,
            String profilePath,
            boolean isCurrentUser
    ) {
        public static User from(UserInfo user, Long currentUserId) {
            return User.builder()
                    .id(user.id())
                    .nickname(user.nickname())
                    .profilePath(user.profilePath())
                    .isCurrentUser(Objects.equals(user.id(), currentUserId))
                    .build();
        }
    }

}
