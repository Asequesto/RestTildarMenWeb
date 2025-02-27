package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.dto.JobRequestDto;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.JobRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/job-request")
public class JobRequestController {

    private final JobRequestService jobRequestService;

    @PostMapping("/{employerId}/send/{translatorId}/job/{jobId}")
    public ResponseEntity<ApiResponse> sendJobRequest(@PathVariable Long employerId,
                                                      @PathVariable Long translatorId,
                                                      @PathVariable Long jobId) {
        try {
            JobRequestDto jobRequest =  jobRequestService.sendRequest(employerId, translatorId, jobId);
            return ResponseEntity.ok(new ApiResponse("Success", jobRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PutMapping("/{jobId}/request")
    public ResponseEntity<ApiResponse> responseJobRequest(@PathVariable Long jobId, @RequestParam String status) {
        try {
            jobRequestService.updateRequestStatus(jobId, status);
            return ResponseEntity.ok(new ApiResponse("Done", "Request " + status));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }

    }

}
