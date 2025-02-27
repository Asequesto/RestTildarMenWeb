package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.dto.JobApplicationDto;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/job-application")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    @PostMapping("/{translatorId}/send/{jobId}")
    public ResponseEntity<ApiResponse> sendJobApplication(@PathVariable Long translatorId,
                                                          @PathVariable Long jobId) {

        try {
            JobApplicationDto application = jobApplicationService.sendApplication(translatorId, jobId);
            return ResponseEntity.ok(new ApiResponse("Success", application));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PutMapping("/{jobId}/application")
    public ResponseEntity<ApiResponse> responseJobApplication(@PathVariable Long jobId, @RequestParam String status) {
        try {
            jobApplicationService.updateApplicationStatus(jobId, status);
            return ResponseEntity.ok(new ApiResponse("Done", "Application " + status));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }

    }

}
