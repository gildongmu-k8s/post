package gildongmu.trip.domain.post.constant;

import lombok.Getter;

@Getter
public enum MemberGender {
    FEMALE,
    MALE,
    NONE;

    /*
    private final String code;

    MemberGender(String code) {
        this.code = code;
    }

    public static MemberGender from(String code) {
        return Arrays.stream(values())
                .filter(value -> value.code.equals(code))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Not Found Gender of Member"));
    }
    */
}
