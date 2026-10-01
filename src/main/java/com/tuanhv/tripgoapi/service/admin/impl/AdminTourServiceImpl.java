package com.tuanhv.tripgoapi.service.admin.impl;

import com.tuanhv.tripgoapi.dto.admin.AdminItineraryForm;
import com.tuanhv.tripgoapi.dto.admin.AdminTourForm;
import com.tuanhv.tripgoapi.dto.admin.AdminTourFormOptions;
import com.tuanhv.tripgoapi.entity.*;
import com.tuanhv.tripgoapi.exception.ConflictException;
import com.tuanhv.tripgoapi.exception.ResourceNotFoundException;
import com.tuanhv.tripgoapi.repository.*;
import com.tuanhv.tripgoapi.service.admin.AdminTourService;
import com.tuanhv.tripgoapi.specification.TourSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminTourServiceImpl implements AdminTourService {

    private final TourRepository tourRepository;
    private final TourDepartureRepository tourDepartureRepository;
    private final ReviewRepository reviewRepository;
    private final DestinationRepository destinationRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Tour> search(String q, int page, int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.min(Math.max(size, 1), 100);

        Pageable pageable = PageRequest.of(
                safePage - 1,
                safeSize,
                Sort.by(
                        Sort.Direction.DESC,
                        "createdAt"
                )
        );

        return tourRepository.findAll(
                TourSpecification.adminKeyword(q),
                pageable
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AdminTourFormOptions getFormOptions() {
        List<AdminTourFormOptions.DestinationOption> destinations =
                destinationRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))
                        .stream()
                        .map(destination ->
                                new AdminTourFormOptions.DestinationOption(
                                        destination.getId(),
                                        destination.getName()
                                )
                        )
                        .toList();

        List<AdminTourFormOptions.CategoryOption> categories =
                categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))
                        .stream()
                        .map(category ->
                                new AdminTourFormOptions.CategoryOption(
                                        category.getId(),
                                        category.getName()
                                )
                        )
                        .toList();

        return new AdminTourFormOptions(
                destinations,
                categories
        );
    }

    @Override
    @Transactional
    public Tour create(AdminTourForm form) {
        Destination destination = destinationRepository.findById(form.getDestinationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "DESTINATION_NOT_FOUND",
                                "Không tìm thấy điểm đến"
                        )
                );

        Category category = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "CATEGORY_NOT_FOUND",
                                "Không tìm thấy loại hình tour"
                        )
                );

        if (tourRepository.existsBySlug(form.getSlug().trim())) {
            throw new ConflictException(
                    "TOUR_SLUG_EXISTS",
                    "Slug tour đã tồn tại"
            );
        }

        Tour tour = new Tour();
        tour.setTitle(form.getTitle().trim());
        tour.setSlug(form.getSlug().trim());
        tour.setDestination(destination);
        tour.setCategory(category);
        tour.setPrice(form.getPrice());
        tour.setDiscountPrice(normalizeDiscountPrice(form.getDiscountPrice()));
        tour.setDurationDays(form.getDurationDays());
        tour.setMaxGroupSize(form.getMaxGroupSize());
        tour.setDescription(form.getDescription().trim());
        tour.setThumbnail(normalizeText(form.getThumbnail()));
        tour.setFeatured(Boolean.TRUE.equals(form.getFeatured()));
        tour.setRating(BigDecimal.ZERO);
        tour.setReviewCount(0);
        tour.setCreatedAt(Instant.now());

        replaceItineraries(
                tour,
                form.getItineraries()
        );

        replaceImages(
                tour,
                form.getImages()
        );

        return tourRepository.save(tour);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminTourForm getEditForm(Long id) {
        Tour tour = tourRepository.findByIdWithAdminDetails(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "TOUR_NOT_FOUND",
                                "Không tìm thấy tour"
                        )
                );

        AdminTourForm form = new AdminTourForm();
        form.setTitle(tour.getTitle());
        form.setSlug(tour.getSlug());
        form.setDestinationId(tour.getDestination().getId());
        form.setCategoryId(tour.getCategory().getId());
        form.setPrice(tour.getPrice());
        form.setDiscountPrice(tour.getDiscountPrice());
        form.setDurationDays(tour.getDurationDays());
        form.setMaxGroupSize(tour.getMaxGroupSize());
        form.setDescription(tour.getDescription());
        form.setThumbnail(tour.getThumbnail());
        form.setFeatured(tour.isFeatured());
        form.setImages(tour.getImages().stream()
                .map(
                        TourImage::getFileName
                )
                .toList()
        );

        List<AdminItineraryForm> itineraries = tour.getItineraries()
                        .stream()
                        .sorted(
                                Comparator.comparing(
                                        TourItinerary::getDayNumber
                                )
                        )
                        .map(item -> {
                            AdminItineraryForm result = new AdminItineraryForm();
                            result.setDayNumber(item.getDayNumber());
                            result.setTitle(item.getTitle());
                            result.setDescription(item.getDescription());
                            return result;
                        })
                        .collect(
                                Collectors.toCollection(
                                        ArrayList::new
                                )
                        );
        form.setItineraries(itineraries);

        if (form.getItineraries().isEmpty()) {
            AdminItineraryForm first = new AdminItineraryForm();
            first.setDayNumber(1);
            form.getItineraries().add(first);
        }

        return form;
    }

    @Override
    @Transactional
    public Tour update(Long id, AdminTourForm form) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "TOUR_NOT_FOUND",
                                "Không tìm thấy tour"
                        )
                );

        Destination destination = destinationRepository.findById(form.getDestinationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "DESTINATION_NOT_FOUND",
                                "Không tìm thấy điểm đến"
                        )
                );

        Category category = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "CATEGORY_NOT_FOUND",
                                "Không tìm thấy loại hình tour"
                        )
                );

        validateUniqueSlugForUpdate(id, form.getSlug());
        tour.setTitle(form.getTitle().trim());
        tour.setSlug(form.getSlug().trim());
        tour.setDestination(destination);
        tour.setCategory(category);
        tour.setPrice(form.getPrice());
        tour.setDiscountPrice(normalizeDiscountPrice(form.getDiscountPrice()));
        tour.setDurationDays(form.getDurationDays());
        tour.setMaxGroupSize(form.getMaxGroupSize());
        tour.setDescription(form.getDescription().trim());
        tour.setThumbnail(normalizeText(form.getThumbnail()));
        tour.setFeatured(
                Boolean.TRUE.equals(
                        form.getFeatured()
                )
        );
        replaceItineraries(tour, form.getItineraries());
        replaceImages(tour, form.getImages());

        return tour;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "NOT_FOUND",
                                "Không tìm thấy tour"
                        )
                );

        if (tourDepartureRepository.existsByTour_Id(id)) {
            throw new ConflictException(
                    "TOUR_IN_USE",
                    "Không thể xóa tour đã có lịch khởi hành"
            );
        }

        if (reviewRepository.existsByTour_Id(id)) {
            throw new ConflictException(
                    "TOUR_IN_USE",
                    "Không thể xóa tour đã có đánh giá"
            );
        }

        try {
            tourRepository.delete(tour);
            tourRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(
                    "TOUR_IN_USE",
                    "Không thể xóa tour đang có dữ liệu liên quan"
            );
        }
    }

    private BigDecimal normalizeDiscountPrice(BigDecimal value) {
        if (value == null) {
            return null;
        }

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }

        return value;
    }

    private String normalizeText(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();

        return trimmed.isEmpty() ? null : trimmed;
    }

    private void replaceItineraries(Tour tour, List<AdminItineraryForm> items) {
        if (tour.getItineraries() == null) {
            tour.setItineraries(new ArrayList<>());
        } else {
            tour.getItineraries().clear();
        }

        if (items == null || items.isEmpty()) {
            return;
        }

        List<TourItinerary> itineraries = items.stream()
                .filter(this::isValidItineraryRow)
                .sorted(
                        Comparator.comparing(
                                AdminItineraryForm::getDayNumber
                        )
                )
                .map(item ->
                        new TourItinerary(
                                item.getDayNumber(),
                                normalizeText(item.getTitle()),
                                normalizeText(item.getDescription())
                        )
                )
                .toList();

        tour.getItineraries().addAll(itineraries);
    }

    private boolean isValidItineraryRow(AdminItineraryForm item) {
        if (item == null) {
            return false;
        }

        return item.getDayNumber() != null
                || normalizeText(item.getTitle()) != null
                || normalizeText(item.getDescription()) != null;
    }

    private void replaceImages(Tour tour, List<String> images) {
        if (tour.getImages() == null) {
            tour.setImages(new ArrayList<>());
        } else {
            tour.getImages().clear();
        }

        if (images == null || images.isEmpty()) {
            return;
        }

        List<TourImage> tourImages = images.stream()
                .map(this::normalizeText)
                .filter(Objects::nonNull)
                .distinct()
                .map(TourImage::new)
                .toList();

        tour.getImages().addAll(tourImages);
    }

    private void validateUniqueSlugForUpdate(Long tourId, String slug) {
        String normalizedSlug = slug.trim();

        if (tourRepository.existsBySlugAndIdNot(normalizedSlug, tourId)) {
            throw new ConflictException(
                    "TOUR_SLUG_EXISTS",
                    "Slug tour đã tồn tại"
            );
        }
    }
}
