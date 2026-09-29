package com.tuanhv.tripgoapi.entity;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TourItinerary {

    @Column(nullable = false)
    private int dayNumber;

    private String title;

    private String description;
}
