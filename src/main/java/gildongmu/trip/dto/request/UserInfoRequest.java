package gildongmu.trip.dto.request;

import lombok.Builder;

@Builder
public record UserInfoRequest(
        String email
) {
    public static UserInfoRequest of(String email){
        return UserInfoRequest.builder()
                .email(email)
                .build();
    }
}
