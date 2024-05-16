package gildongmu.trip.dto.transfer;

import gildongmu.trip.dto.response.UserInfoResponse;
import lombok.Builder;

@Builder
public record UserInfo(
        Long id,
        String nickname,
        String profilePath
) {
    public static UserInfo from(UserInfoResponse response) {
        return UserInfo.builder()
                .id(response.id())
                .nickname(response.nickname())
                .profilePath(response.profilePath())
                .build();
    }
}
