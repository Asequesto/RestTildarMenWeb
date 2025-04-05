package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.SpecializationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/specialization")
public class SpecializationController {

    private final SpecializationService specializationService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> allSpecializations() {
        return ResponseEntity.ok(new ApiResponse("Success", specializationService.getAll()));
    }

}
