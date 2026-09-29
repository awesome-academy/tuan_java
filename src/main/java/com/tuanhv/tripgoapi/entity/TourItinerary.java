package com.tuanhv.tripgoapi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TourItinerary {

    @Column(nullable = false)
    private int dayNumber;

    private String title;

    private String description;
}
