package com.tuanhv.tripgoapi.service;

import com.tuanhv.tripgoapi.dto.response.DestinationResponse;

import java.util.List;

public interface DestinationService {

    List<DestinationResponse> getDestinations();
}
