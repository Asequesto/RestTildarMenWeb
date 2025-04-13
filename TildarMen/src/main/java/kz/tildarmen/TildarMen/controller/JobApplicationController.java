package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.dto.JobApplicationDto;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/job-application")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    @PreAuthorize("hasAnyAuthority('TRANSLATOR')")
    @PostMapping("/{translatorId}/send/{jobId}")
    public ResponseEntity<ApiResponse> sendJobApplication(@PathVariable Long translatorId,
                                                          @PathVariable Long jobId) {

        try {
            JobApplicationDto application = jobApplicationService.sendApplication(translatorId, jobId);
            return ResponseEntity.ok(new ApiResponse("Success", application));
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Email Error", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }


    @PreAuthorize("hasAnyAuthority('EMPLOYER')")
    @PutMapping("/{applicationId}/application")
    public ResponseEntity<ApiResponse> responseJobApplication(@PathVariable Long applicationId, @RequestParam String status) {
        try {
            jobApplicationService.updateApplicationStatus(applicationId, status);

            return ResponseEntity.ok(new ApiResponse("Done", "Application " + status));
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Email Error", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

}
