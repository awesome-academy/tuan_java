package com.tuanhv.tripgoapi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tours")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(unique = true)
    private String slug;

    private String thumbnail;

    private int durationDays;

    private int maxGroupSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id", nullable = false)
    private Destination destination;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;    // beach|mountain|city|trekking|cruise

    @Column(
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal price;

    @Column(precision = 19, scale = 2)
    private BigDecimal discountPrice;   // nullable

    @Column(
            nullable = false,
            precision = 2,
            scale = 1
    )
    private BigDecimal rating;

    private int reviewCount;

    @Column(name = "is_featured", nullable = false)
    private boolean isFeatured;

    @ElementCollection
    @CollectionTable(name = "tour_images")
    private List<TourImage> images = new ArrayList<>();

    @Column(columnDefinition = "text")
    private String description;

    @ElementCollection
    @CollectionTable(name = "tour_itineraries")
    private List<TourItinerary> itineraries = new ArrayList<>();

    @OneToMany(mappedBy = "tour")
    private List<TourDeparture> departures;

    @CreationTimestamp
    private Instant createdAt;
}
