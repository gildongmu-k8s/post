package gildongmu.trip.dto.request;


import gildongmu.trip.domain.post.constant.MemberGender;
import gildongmu.trip.dto.TripDate;
import gildongmu.trip.util.validator.EnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PostCreateRequest(
        @NotBlank(message = "Title is required.")
        String title,

        @NotBlank(message = "Destination is required.")
        String destination,

        @NotNull(message = "Trip Date cannot be null")
        TripDate tripDate,

        Short numberOfPeople,

        @NotNull(message = "Member Gender cannot be null")
        @EnumValue(enumClass = MemberGender.class, message = "invalid gender")
        String gender,

        @NotBlank(message = "Content is required.")
        String content,

        List<String> tag

) {
}
