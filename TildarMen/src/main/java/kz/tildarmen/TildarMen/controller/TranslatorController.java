package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.dto.*;
import kz.tildarmen.TildarMen.mapper.TranslatorMapper;
import kz.tildarmen.TildarMen.requests.UpdatePasswordRequest;
import kz.tildarmen.TildarMen.requests.UpdateUserRequest;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/translator")
public class TranslatorController {

    private final TranslatorService translatorService;
    private final WorkExperienceService workExperienceService;
    private final UserService userService;
    private final TranslatorMapper translatorMapper;
    private final EducationService educationService;
    private final CertificateService certificateService;

    @GetMapping("/{id}/settings")
    public ResponseEntity<ApiResponse> getTranslatorSettingsById(@PathVariable Long id) {
        try {
            UserDto user = userService.getUserById(id);
            return ResponseEntity.ok(new ApiResponse("Success", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<ApiResponse> getTranslatorProfileById(@PathVariable Long id) {
        try {
            TranslatorDto translator = translatorMapper.toDto(translatorService.getTranslatorById(id));
            return ResponseEntity.ok(new ApiResponse("Success", translator));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }
    @PostMapping("/{id}/work")
    public ResponseEntity<ApiResponse> createWorkExperience(@PathVariable Long id,
                                                            @RequestBody WorkExperienceDto request){
        try {
            WorkExperienceDto work = workExperienceService.addWorkExperience(request, id);
            return ResponseEntity.ok(new ApiResponse("Successfully created work experience", work));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/education")
    public ResponseEntity<ApiResponse> createEducation(@PathVariable Long id, @RequestBody EducationDto request){
        try {
            EducationDto education = educationService.addEducation(id, request);
            return ResponseEntity.ok(new ApiResponse("Successfully created education", education));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/certificate")
    public ResponseEntity<ApiResponse> createCertificate(@PathVariable Long id, @RequestBody CertificateDto request){
        try {
            CertificateDto certificate = certificateService.addCertificate(id, request);
            return ResponseEntity.ok(new ApiResponse("Successfully created certificate", certificate));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponse> updateTranslatorPassword(@PathVariable Long id,
                                                                @RequestBody UpdatePasswordRequest request){
        try {
            translatorService.updatePassword(id, request);
            return ResponseEntity.ok(new ApiResponse("Password updated", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Oops", e.getMessage()));
        }
    }

    @PutMapping("/{id}/settings")
    public ResponseEntity<ApiResponse> updateTranslatorAccountSettings(@RequestBody UpdateUserRequest request,
                                                                       @PathVariable Long id){
        try {
            UserDto user = translatorService.updateTranslatorAccountSettings(request, id);
            return ResponseEntity.ok(new ApiResponse("Successfully updated translator", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Oops", e.getMessage()));
        }
    }

    @PutMapping("/{id}/work/{workId}/update")
    public ResponseEntity<ApiResponse> updateWorkExperience(@PathVariable Long id,
                                                            @PathVariable Long workId,
                                                            @RequestBody WorkExperienceDto request){
        try {
            WorkExperienceDto work = workExperienceService.updateWorkExperience(request, id, workId);
            return ResponseEntity.ok(new ApiResponse("Successfully updated work experience", work));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/education/{educationId}")
    public ResponseEntity<ApiResponse> updateEducation(@PathVariable Long id,
                                                       @PathVariable Long educationId,
                                                       @RequestBody EducationDto request){
        try {
            EducationDto education = educationService.updateEducation(id, educationId, request);
            return ResponseEntity.ok(new ApiResponse("Successfully updated education", education));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/certificate/{certificateId}")
    public ResponseEntity<ApiResponse> updateCertificate(@PathVariable Long id,
                                                         @PathVariable Long certificateId,
                                                         @RequestBody CertificateDto request){
        try {
            CertificateDto certificate = certificateService.updateCertificate(id, certificateId, request);
            return ResponseEntity.ok(new ApiResponse("Successfully updated certificate", certificate));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }


    @DeleteMapping("/{id}/work/{workId}/delete")
    public ResponseEntity<ApiResponse> deleteWorkExperience(@PathVariable Long id, @PathVariable Long workId){
        try {
            workExperienceService.deleteWorkExperience(id, workId);
            return ResponseEntity.ok(new ApiResponse("Successfully updated work experience", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/education/{educationId}")
    public ResponseEntity<ApiResponse> deleteEducation(@PathVariable Long id, @PathVariable Long educationId){
        try {
            educationService.deleteEducation(id, educationId);
            return ResponseEntity.ok(new ApiResponse("Successfully deleted education", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/certificate/{certificateId}")
    public ResponseEntity<ApiResponse> deleteCertificate(@PathVariable Long id,
                                                         @PathVariable Long certificateId){
        try {
            certificateService.deleteCertificate(id, certificateId);
            return ResponseEntity.ok(new ApiResponse("Successfully deleted certificate", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse> deleteTranslator(@PathVariable Long id){
        try {
            translatorService.deleteTranslator(id);
            return ResponseEntity.ok(new ApiResponse("Successfully deleted translator", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

}
