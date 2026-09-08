package com.tuanhv.tripgoapi.service.impl;

import com.tuanhv.tripgoapi.dto.request.TourSearchRequest;
import com.tuanhv.tripgoapi.dto.response.TourAvailabilityResponse;
import com.tuanhv.tripgoapi.dto.response.TourDetailResponse;
import com.tuanhv.tripgoapi.dto.response.TourPageResponse;
import com.tuanhv.tripgoapi.dto.response.TourResponse;
import com.tuanhv.tripgoapi.entity.Tour;
import com.tuanhv.tripgoapi.entity.TourDeparture;
import com.tuanhv.tripgoapi.entity.TourItinerary;
import com.tuanhv.tripgoapi.exception.BadRequestException;
import com.tuanhv.tripgoapi.exception.ResourceNotFoundException;
import com.tuanhv.tripgoapi.mapper.TourMapper;
import com.tuanhv.tripgoapi.repository.TourDepartureRepository;
import com.tuanhv.tripgoapi.repository.TourRepository;
import com.tuanhv.tripgoapi.repository.projection.DepartureAvailabilityProjection;
import com.tuanhv.tripgoapi.service.TourService;
import com.tuanhv.tripgoapi.specification.TourSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;
    private final TourDepartureRepository tourDepartureRepository;
    private final TourMapper tourMapper;

    @Override
    public TourPageResponse searchTours(TourSearchRequest request) {
        validate(request);

        Specification<Tour> specification = TourSpecification.search(request);

        Sort sort = resolveSort(request.getSort());

        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getLimit(),
                sort
        );

        Page<Tour> result = tourRepository.findAll(specification, pageable);

        List<TourResponse> data = result.getContent()
                .stream()
                .map(tourMapper::toResponse)
                .toList();

        return new TourPageResponse(
                data,
                result.getTotalElements(),
                request.getPage(),
                result.getSize()
        );
    }

    @Override
    public TourDetailResponse getTourDetail(String slug) {
        Tour tour = tourRepository.findDetailBySlug(slug)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "NOT_FOUND",
                                "Không tìm thấy tour"
                        )
                );

        Long tourId = tour.getId();

        List<String> images =
                tourRepository.findImagesByTourId(tourId);

        List<TourItinerary> itinerary =
                tourRepository.findItineraryByTourId(tourId);

        List<TourDeparture> departures =
                tourDepartureRepository.findUpcomingByTourId(tourId);

        return tourMapper.toDetailResponse(
                tour,
                images,
                itinerary,
                departures
        );
    }

    @Override
    public List<TourAvailabilityResponse> getTourAvailability(String slug, String month) {
        if (!tourRepository.existsBySlug(slug)) {
            throw new ResourceNotFoundException(
                    "NOT_FOUND",
                    "Không tìm thấy tour"
            );
        }

        YearMonth yearMonth = parseMonth(month);

        LocalDate fromDate = yearMonth.atDay(1);
        LocalDate toDate = yearMonth.plusMonths(1).atDay(1);

        List<DepartureAvailabilityProjection> rows = tourDepartureRepository.findAvailability(
                slug,
                fromDate,
                toDate
        );

        return rows.stream()
                .map(row ->
                        new TourAvailabilityResponse(
                                row.getDate(),
                                row.getSlotsLeft().intValue(),
                                row.getPrice()
                        )
                ).toList();
    }

    private void validate(TourSearchRequest request) {
        if (request.getMinPrice() != null
                && request.getMaxPrice() != null
                && request.getMinPrice().compareTo(request.getMaxPrice()) > 0) {
            throw new BadRequestException(
                    "INVALID_PRICE_RANGE",
                    "minPrice must not be greater than maxPrice"
            );
        }
    }

    private Sort resolveSort(String sort) {
        return switch (sort) {

            case "price_asc" ->
                    Sort.by("price").ascending();

            case "price_desc" ->
                    Sort.by("price").descending();

            case "rating" ->
                    Sort.by(
                            Sort.Order.desc("rating"),
                            Sort.Order.desc("reviewCount")
                    );

            case "newest" ->
                    Sort.by("createdAt").descending();

            default ->
                    throw new BadRequestException(
                            "INVALID_SORT",
                            "Unsupported sort: " + sort
                    );
        };
    }

    private YearMonth parseMonth(String month) {
        try {
            return YearMonth.parse(month);
        } catch (DateTimeParseException ex) {
            throw new BadRequestException(
                    "INVALID_MONTH",
                    "month phải có định dạng yyyy-MM"
            );
        }
    }
}
