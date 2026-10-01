package com.tuanhv.tripgoapi.controller.api;

import com.tuanhv.tripgoapi.dto.response.DestinationResponse;
import com.tuanhv.tripgoapi.service.api.DestinationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/destinations")
@RequiredArgsConstructor
@Tag(name = "02. Destinations")
public class DestinationController {

    private final DestinationService destinationService;

    @GetMapping
    public List<DestinationResponse> getDestinations() {
        return destinationService.getDestinations();
    }

}
