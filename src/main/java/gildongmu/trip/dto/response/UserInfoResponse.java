package gildongmu.trip.dto.response;

import lombok.Builder;

@Builder
public record UserInfoResponse(
        Long id,
        String nickname,
        String profilePath
) {
}
