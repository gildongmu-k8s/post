package gildongmu.trip.application;

import gildongmu.trip.client.UserClient;
import gildongmu.trip.dto.transfer.UserInfo;
import gildongmu.trip.dto.request.UserInfoRequest;
import gildongmu.trip.util.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAdapter {
    private final UserClient userClient;

    public UserInfo getUserInfoFromToken(String token) {
        String email = JwtTokenManager.parseEmail(token);
        return UserInfo.from(userClient.getUserInfoFromEmail(UserInfoRequest.of(email), token));
    }

    public UserInfo getUserInfoFromId(Long id) {
        return UserInfo.from(userClient.getUserInfoFromId(id));
    }
}
