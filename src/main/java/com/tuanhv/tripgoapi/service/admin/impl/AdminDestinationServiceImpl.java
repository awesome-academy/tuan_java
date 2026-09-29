package com.tuanhv.tripgoapi.service.admin.impl;

import com.tuanhv.tripgoapi.dto.admin.AdminDestinationForm;
import com.tuanhv.tripgoapi.dto.admin.AdminDestinationListItem;
import com.tuanhv.tripgoapi.entity.Destination;
import com.tuanhv.tripgoapi.exception.ConflictException;
import com.tuanhv.tripgoapi.exception.ResourceNotFoundException;
import com.tuanhv.tripgoapi.repository.DestinationRepository;
import com.tuanhv.tripgoapi.repository.TourRepository;
import com.tuanhv.tripgoapi.repository.projection.DestinationTourCountProjection;
import com.tuanhv.tripgoapi.service.admin.AdminDestinationService;
import com.tuanhv.tripgoapi.specification.DestinationSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminDestinationServiceImpl implements AdminDestinationService {

    private final DestinationRepository destinationRepository;
    private final TourRepository tourRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<AdminDestinationListItem> search(String q, int page, int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.min(Math.max(size, 1), 100);

        Pageable pageable = PageRequest.of(
                safePage - 1,
                safeSize,
                Sort.by(Sort.Direction.ASC, "name")
        );

        Page<Destination> destinations = destinationRepository.findAll(
                DestinationSpecification.keyword(q),
                pageable
        );

        if (destinations.isEmpty()) {
            return destinations.map(
                    this::toListItemWithoutCount
            );
        }

        List<Long> ids = destinations.getContent()
                .stream()
                .map(Destination::getId)
                .toList();

        Map<Long, Long> counts = tourRepository.countByDestinationIds(ids)
                .stream()
                .collect(
                        Collectors.toMap(
                                DestinationTourCountProjection::getDestinationId,
                                DestinationTourCountProjection::getTourCount
                        )
                );

        return destinations.map(destination ->
                new AdminDestinationListItem(
                        destination.getId(),
                        destination.getName(),
                        destination.getSlug(),
                        destination.getImage(),
                        counts.getOrDefault(
                                destination.getId(),
                                0L
                        )
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AdminDestinationForm getEditForm(Long id) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "DESTINATION_NOT_FOUND",
                                "Không tìm thấy điểm đến"
                        )
                );

        AdminDestinationForm form = new AdminDestinationForm();
        form.setName(destination.getName());
        form.setSlug(destination.getSlug());
        form.setImageUrl(destination.getImage());

        return form;
    }

    @Override
    @Transactional
    public void create(AdminDestinationForm form) {
        String slug = form.getSlug().trim();

        if (destinationRepository.existsBySlug(slug)) {
            throw new ConflictException(
                    "DESTINATION_SLUG_EXISTS",
                    "Slug điểm đến đã tồn tại"
            );
        }

        Destination destination = new Destination();
        destination.setName(form.getName().trim());
        destination.setSlug(slug);
        destination.setImage(normalizeText(form.getImageUrl()));

        destinationRepository.save(destination);
    }

    @Override
    @Transactional
    public void update(Long id, AdminDestinationForm form) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "DESTINATION_NOT_FOUND",
                                "Không tìm thấy điểm đến"
                        )
                );

        String slug = form.getSlug().trim();

        if (destinationRepository.existsBySlugAndIdNot(slug, id)) {
            throw new ConflictException(
                    "DESTINATION_SLUG_EXISTS",
                    "Slug điểm đến đã tồn tại"
            );
        }

        destination.setName(form.getName().trim());
        destination.setSlug(slug);
        destination.setImage(normalizeText(form.getImageUrl()));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "DESTINATION_NOT_FOUND",
                                "Không tìm thấy điểm đến"
                        )
                );

        if (tourRepository.existsByDestination_Id(id)) {
            throw new ConflictException(
                    "DESTINATION_IN_USE",
                    "Không thể xóa điểm đến đang được tour sử dụng"
            );
        }

        destinationRepository.delete(destination);
    }

    private AdminDestinationListItem toListItemWithoutCount(Destination destination) {
        return new AdminDestinationListItem(
                destination.getId(),
                destination.getName(),
                destination.getSlug(),
                destination.getImage(),
                0
        );
    }

    private String normalizeText(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();

        return trimmed.isEmpty() ? null : trimmed;
    }
}
