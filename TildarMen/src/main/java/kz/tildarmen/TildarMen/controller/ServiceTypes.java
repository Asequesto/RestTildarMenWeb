package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.ServiceTypesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/service-types")
public class ServiceTypes {

    private final ServiceTypesService serviceTypesService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> allServiceTypes() {
        return ResponseEntity.ok(new ApiResponse("Success", serviceTypesService.getAll()));
    }

}
