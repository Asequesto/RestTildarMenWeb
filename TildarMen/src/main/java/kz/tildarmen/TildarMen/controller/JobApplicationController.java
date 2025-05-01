package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.dto.JobApplicationDto;
import kz.tildarmen.TildarMen.model.User;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.AuthService;
import kz.tildarmen.TildarMen.services.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/job-application")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;
    private final AuthService authService;

    @PreAuthorize("hasAnyAuthority('TRANSLATOR')")
    @PostMapping("/{translatorId}/send/{jobId}")
    public ResponseEntity<ApiResponse> sendJobApplication(@PathVariable Long translatorId,
                                                          @PathVariable Long jobId,
                                                          @AuthenticationPrincipal User userDetails) {

        try {
            authService.checkPermission(userDetails, translatorId);
            JobApplicationDto application = jobApplicationService.sendApplication(translatorId, jobId);
            return ResponseEntity.ok(new ApiResponse("Success", application));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Email Error", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }


    @PreAuthorize("hasAnyAuthority('EMPLOYER')")
    @PutMapping("/{applicationId}/application")
    public ResponseEntity<ApiResponse> responseJobApplication(@PathVariable Long applicationId,
                                                              @RequestParam String status,
                                                              @AuthenticationPrincipal User userDetails) {
        try {
            jobApplicationService.updateApplicationStatus(applicationId, status, userDetails);
            return ResponseEntity.ok(new ApiResponse("Done", "Application " + status));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Email Error", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

}
