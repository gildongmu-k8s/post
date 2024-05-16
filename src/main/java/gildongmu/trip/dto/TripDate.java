package gildongmu.trip.dto;

import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
public record TripDate(
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate startDate,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate endDate
) {

    public static TripDate of(LocalDate startDate, LocalDate endDate) {
        return TripDate.builder()
            .startDate(startDate)
            .endDate(endDate)
            .build();
    }
/*
    public static List<TripDate> toList(LocalDate startDate, LocalDate endDate) {
        return List.of(TripDate.builder()
            .startDate(startDate)
            .endDate(endDate)
            .build());
    }
 */
}
