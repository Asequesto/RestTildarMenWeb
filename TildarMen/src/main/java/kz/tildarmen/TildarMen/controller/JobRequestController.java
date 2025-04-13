package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.dto.JobRequestDto;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.JobRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/job-request")
public class JobRequestController {

    private final JobRequestService jobRequestService;

    @PreAuthorize("hasAnyAuthority('EMPLOYER')")
    @PostMapping("/{employerId}/send/{translatorId}/job/{jobId}")
    public ResponseEntity<ApiResponse> sendJobRequest(@PathVariable Long employerId,
                                                      @PathVariable Long translatorId,
                                                      @PathVariable Long jobId) {
        try {
            JobRequestDto jobRequest =  jobRequestService.sendRequest(employerId, translatorId, jobId);
            return ResponseEntity.ok(new ApiResponse("Success", jobRequest));
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Email Error", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('TRANSLATOR')")
    @PutMapping("/{requestId}/request")
    public ResponseEntity<ApiResponse> responseJobRequest(@PathVariable Long requestId, @RequestParam String status) {
        try {
            jobRequestService.updateRequestStatus(requestId, status);
            return ResponseEntity.ok(new ApiResponse("Done", "Request " + status));
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Email Error", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }

    }

}
