package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/location")
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCities() {
        return ResponseEntity.ok(new ApiResponse("Success", locationService.getAllCities()));
    }

}
