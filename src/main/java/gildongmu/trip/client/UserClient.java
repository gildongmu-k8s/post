package gildongmu.trip.client;

import gildongmu.trip.dto.request.UserInfoRequest;
import gildongmu.trip.dto.response.UserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "userClient")
public interface UserClient {
    @GetMapping(value = "/users")
    UserInfoResponse getUserInfoFromEmail(@RequestBody UserInfoRequest request, @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token);

    @GetMapping(value = "/users/{id}")
    UserInfoResponse getUserInfoFromId(@PathVariable Long id);
}
