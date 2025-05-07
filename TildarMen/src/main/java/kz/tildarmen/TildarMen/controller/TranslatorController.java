package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.dto.*;
import kz.tildarmen.TildarMen.mapper.TranslatorMapper;
import kz.tildarmen.TildarMen.mapper.TranslatorSettingsMapper;
import kz.tildarmen.TildarMen.model.Translator;
import kz.tildarmen.TildarMen.model.User;
import kz.tildarmen.TildarMen.repository.TranslatorRepository;
import kz.tildarmen.TildarMen.requests.*;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/translator")
@PreAuthorize("hasAuthority('TRANSLATOR')")
public class TranslatorController {

    private final TranslatorService translatorService;
    private final WorkExperienceService workExperienceService;
    private final TranslatorMapper translatorMapper;
    private final EducationService educationService;
    private final CertificateService certificateService;
    private final ImageService imageService;
    private final JobApplicationService jobApplicationService;
    private final JobRequestService jobRequestService;
    private final TranslatorSettingsMapper translatorSettingsMapper;
    private final TranslatorRepository translatorRepository;
    private final AuthService authService;


    @GetMapping("/{id}/settings")
    public ResponseEntity<ApiResponse> getTranslatorSettingsById(@PathVariable Long id,
                                                                 @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            Translator translator = translatorService.getTranslatorById(id);
            return ResponseEntity.ok(new ApiResponse("Success", translatorSettingsMapper.toDto(translator)));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<ApiResponse> getTranslatorProfileById(@PathVariable Long id,
                                                                @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            TranslatorDto translator = translatorMapper.toDto(translatorService.getTranslatorById(id));
            return ResponseEntity.ok(new ApiResponse("Success", translator));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/requests")
    public ResponseEntity<ApiResponse> getTranslatorRequestsById(@PathVariable Long id,
                                                                 @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            List<JobRequestDto> requests = jobRequestService.getTranslatorRequests(id);
            return ResponseEntity.ok(new ApiResponse("Success", requests));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/applications")
    public ResponseEntity<ApiResponse> getTranslatorApplicationsById(@PathVariable Long id,
                                                                     @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            return ResponseEntity.ok(new ApiResponse("Success",  jobApplicationService.getTranslatorApplications(id)));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/work")
    public ResponseEntity<ApiResponse> createWorkExperience(@PathVariable Long id,
                                                            @RequestBody WorkExperienceDto request,
                                                            @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            WorkExperienceDto work = workExperienceService.addWorkExperience(request, id);
            return ResponseEntity.ok(new ApiResponse("Successfully created work experience", work));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/education")
    public ResponseEntity<ApiResponse> createEducation(@PathVariable Long id,
                                                       @RequestParam MultipartFile file,
                                                       @RequestPart UploadEducationRequest request,
                                                       @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            EducationDto education = educationService.addEducation(id, file, request);
            return ResponseEntity.ok(new ApiResponse("Successfully created education", education));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/certificate")
    public ResponseEntity<ApiResponse> createCertificate(@PathVariable Long id,
                                                         @RequestParam MultipartFile file,
                                                         @RequestPart UploadCertificateRequest request,
                                                         @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            CertificateDto certificate = certificateService.addCertificate(id, file, request);
            return ResponseEntity.ok(new ApiResponse("Successfully created certificate", certificate));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/project")
    public ResponseEntity<ApiResponse> uploadProject(@PathVariable Long id,
                                                     @RequestParam MultipartFile file,
                                                     @AuthenticationPrincipal User userDetails) {

        try {
            authService.checkPermission(userDetails, id);
            String url = imageService.uploadFile(id, file, "project");
            return ResponseEntity.ok(new ApiResponse("Success", url));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/video")
    public ResponseEntity<ApiResponse> uploadVideo(@PathVariable Long id,
                                                   @RequestParam MultipartFile file,
                                                   @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            String url = imageService.uploadFile(id, file, "video");
            return ResponseEntity.ok(new ApiResponse("Success", url));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponse> updateTranslatorPassword(@PathVariable Long id,
                                                                @RequestBody UpdatePasswordRequest request,
                                                                @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            translatorService.updatePassword(id, request);
            return ResponseEntity.ok(new ApiResponse("Password updated", null));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Oops", e.getMessage()));
        }
    }

    @PutMapping("/{id}/settings")
    public ResponseEntity<ApiResponse> updateTranslatorAccountSettings(@RequestBody UpdateUserRequest request,
                                                                       @PathVariable Long id,
                                                                       @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            UserDto user = translatorService.updateTranslatorAccountSettings(request, id);
            return ResponseEntity.ok(new ApiResponse("Successfully updated translator", user));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Oops", e.getMessage()));
        }
    }


    @PutMapping("/{id}/profile")
    public ResponseEntity<ApiResponse> updateTranslatorProfile(@PathVariable Long id,
                                                               @RequestBody TranslatorProfileRequest request,
                                                               @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            TranslatorDto translator = translatorService.updateTranslatorProfile(id, request);
            return ResponseEntity.ok(new ApiResponse("Successfully updated translator", translator));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Oops", e.getMessage()));
        }
    }

    @PutMapping("/{id}/intro")
    public ResponseEntity<ApiResponse> updateTranslatorIntro(@PathVariable Long id,
                                                             @RequestBody UpdateIntroRequest intro,
                                                             @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            TranslatorDto translator = translatorService.addIntroduction(id, intro);
            return ResponseEntity.ok(new ApiResponse("Successfully updated language", translator));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/language")
    public ResponseEntity<ApiResponse> updateTranslatorLanguage(@PathVariable Long id, @RequestParam String language,
                                                                @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            TranslatorDto translator = translatorService.addLanguage(id, language);
            return ResponseEntity.ok(new ApiResponse("Successfully updated language", translator));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/service")
    public ResponseEntity<ApiResponse> updateTranslatorService(@PathVariable Long id,
                                                               @RequestParam String service,
                                                               @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            TranslatorDto translator = translatorService.addService(id, service);
            return ResponseEntity.ok(new ApiResponse("Successfully updated service", translator));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/specialization")
    public ResponseEntity<ApiResponse> updateTranslatorSpecialization(@PathVariable Long id,
                                                                      @RequestParam String specialization,
                                                                      @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            TranslatorDto translator = translatorService.addSpecialization(id, specialization);
            return ResponseEntity.ok(new ApiResponse("Successfully updated specialization", translator));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/work/{workId}/update")
    public ResponseEntity<ApiResponse> updateWorkExperience(@PathVariable Long id,
                                                            @PathVariable Long workId,
                                                            @RequestBody WorkExperienceDto request,
                                                            @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            WorkExperienceDto work = workExperienceService.updateWorkExperience(request, id, workId);
            return ResponseEntity.ok(new ApiResponse("Successfully updated work experience", work));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/education/{educationId}")
    public ResponseEntity<ApiResponse> updateEducation(@PathVariable Long id,
                                                       @PathVariable Long educationId,
                                                       @RequestParam MultipartFile file,
                                                       @RequestPart UploadEducationRequest request,
                                                       @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            EducationDto education = educationService.updateEducation(id, educationId, file, request);
            return ResponseEntity.ok(new ApiResponse("Successfully updated education", education));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/profile-image")
    public ResponseEntity<ApiResponse> uploadProfileImage(@PathVariable Long id,
                                                          @RequestParam MultipartFile file,
                                                          @AuthenticationPrincipal User userDetails) {

        try {
            authService.checkPermission(userDetails, id);
            Translator translator = translatorService.getTranslatorById(id);
            if(translator.getProfileImageUrl() != null){
                imageService.deleteImage(translator.getProfileImageUrl());
                translator.setProfileImageUrl(null);
            }
            String url = imageService.uploadFile(id, file, "profile-image");
            return ResponseEntity.ok(new ApiResponse("Success", url));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/degree")
    public ResponseEntity<ApiResponse> uploadDegree(@PathVariable Long id,
                                                    @RequestParam MultipartFile file,
                                                    @AuthenticationPrincipal User userDetails) {

        try {
            authService.checkPermission(userDetails, id);
            String url = imageService.uploadFile(id, file, "degree");
            return ResponseEntity.ok(new ApiResponse("Success", url));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/certificate/{certificateId}")
    public ResponseEntity<ApiResponse> updateCertificate(@PathVariable Long id,
                                                         @PathVariable Long certificateId,
                                                         @RequestParam MultipartFile file,
                                                         @RequestPart UploadCertificateRequest request,
                                                         @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            CertificateDto certificate = certificateService.updateCertificate(id, certificateId, file, request);
            return ResponseEntity.ok(new ApiResponse("Successfully updated certificate", certificate));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/video")
    public ResponseEntity<ApiResponse> deleteVideo(@PathVariable Long id,
                                                   @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            Translator translator = translatorService.getTranslatorById(id);
            imageService.deleteImage(translator.getVideoUrl());
            translator.setVideoUrl(null);
            translatorRepository.save(translator);
            return ResponseEntity.ok(new ApiResponse("Successfully deleted video", null));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/language")
    public ResponseEntity<ApiResponse> deleteTranslatorLanguage(@PathVariable Long id, @RequestParam String language,
                                                                @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            translatorService.deleteLanguage(id, language);
            return ResponseEntity.ok(new ApiResponse("Successfully deleted language", null));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/service")
    public ResponseEntity<ApiResponse> deleteTranslatorService(@PathVariable Long id,
                                                               @RequestParam String service,
                                                               @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            translatorService.deleteService(id, service);
            return ResponseEntity.ok(new ApiResponse("Successfully deleted service", null));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/specialization")
    public ResponseEntity<ApiResponse> deleteTranslatorSpecialization(@PathVariable Long id,
                                                                      @RequestParam String specialization,
                                                                      @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            translatorService.deleteSpecialization(id, specialization);
            return ResponseEntity.ok(new ApiResponse("Successfully deleted specialization", null));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }


    @DeleteMapping("/{id}/work/{workId}/delete")
    public ResponseEntity<ApiResponse> deleteWorkExperience(@PathVariable Long id, @PathVariable Long workId,
                                                            @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            workExperienceService.deleteWorkExperience(id, workId);
            return ResponseEntity.ok(new ApiResponse("Successfully updated work experience", null));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/education/{educationId}")
    public ResponseEntity<ApiResponse> deleteEducation(@PathVariable Long id, @PathVariable Long educationId,
                                                       @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            educationService.deleteEducation(id, educationId);
            return ResponseEntity.ok(new ApiResponse("Successfully deleted education", null));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/certificate/{certificateId}")
    public ResponseEntity<ApiResponse> deleteCertificate(@PathVariable Long id,
                                                         @PathVariable Long certificateId,
                                                         @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            certificateService.deleteCertificate(id, certificateId);
            return ResponseEntity.ok(new ApiResponse("Successfully deleted certificate", null));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/project/{projectId}")
    public ResponseEntity<ApiResponse> deleteProject(@PathVariable Long id, @PathVariable int projectId,
                                                     @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            Translator translator = translatorService.getTranslatorById(id);
            imageService.deleteImage(translator.getProjectUrls().get(projectId - 1));
            translator.getProjectUrls().remove(projectId - 1);
            translatorRepository.save(translator);
            return ResponseEntity.ok(new ApiResponse("Successfully deleted project", null));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }

    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse> deleteTranslator(@PathVariable Long id,
                                                        @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            translatorService.deleteTranslator(id);
            return ResponseEntity.ok(new ApiResponse("Successfully deleted translator", null));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

}
