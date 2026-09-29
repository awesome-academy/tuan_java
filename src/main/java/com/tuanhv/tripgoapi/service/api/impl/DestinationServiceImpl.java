package com.tuanhv.tripgoapi.service.api.impl;

import com.tuanhv.tripgoapi.dto.response.DestinationResponse;
import com.tuanhv.tripgoapi.repository.DestinationRepository;
import com.tuanhv.tripgoapi.service.api.DestinationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinationServiceImpl implements DestinationService {

    private final DestinationRepository destinationRepository;

    public DestinationServiceImpl(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    @Override
    public List<DestinationResponse> getDestinations() {
        return destinationRepository.findAllWithTourCount();
    }
}
