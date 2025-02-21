package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.model.Translator;
import kz.tildarmen.TildarMen.requests.UpdateUserRequest;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.TranslatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/translator")
public class TranslatorController {

    private final TranslatorService translatorService;

    @PostMapping("/{id}/update")
    public ResponseEntity<ApiResponse> updateTranslator(@RequestBody UpdateUserRequest request , @PathVariable Long id){
        try {
            Translator translator = translatorService.updateTranslator(request, id);
            return ResponseEntity.ok(new ApiResponse("Successfully updated translator", translator));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Oops", e.getMessage()));
        }
    }

}
