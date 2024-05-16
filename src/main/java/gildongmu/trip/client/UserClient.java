package gildongmu.trip.client;

import gildongmu.trip.dto.request.UserInfoRequest;
import gildongmu.trip.dto.response.UserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "user", url = "localhost:8081")
public interface UserClient {
    @GetMapping(value = "/users")
    UserInfoResponse getUserInfoFromEmail(@RequestBody UserInfoRequest request);

    @GetMapping(value = "/users/{id}")
    UserInfoResponse getUserInfoFromId(@PathVariable Long id);
}
