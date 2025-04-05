package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/language")
public class LanguageController {

    public final LanguageService languageService;

    @GetMapping("all")
    public ResponseEntity<ApiResponse> getAllLanguages() {
        return ResponseEntity.ok(new ApiResponse("Success", languageService.getAllLanguages()));
    }

}
