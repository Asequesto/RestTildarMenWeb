package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.dto.JobRequestDto;
import kz.tildarmen.TildarMen.model.Job;
import kz.tildarmen.TildarMen.model.JobRequest;
import kz.tildarmen.TildarMen.model.User;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.EmailSenderService;
import kz.tildarmen.TildarMen.services.JobRequestService;
import kz.tildarmen.TildarMen.services.UserService;
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
    private final EmailSenderService emailSenderService;
    private final UserService userService;

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

    @PreAuthorize("hasAnyAuthority('TRANSLATOR')")
    @PutMapping("/{requestId}/request")
    public ResponseEntity<ApiResponse> responseJobRequest(@PathVariable Long requestId, @RequestParam String status) {
        try {
            jobRequestService.updateRequestStatus(requestId, status);

            JobRequest jobRequest = jobRequestService.findById(requestId);
            User user = userService.getAuthenticatedUser();
            Job job = jobRequest.getJob();

            String email = jobRequest.getEmployer().getEmail();
            String subject = "Job Request Responded";
            String message = "Your job request for - " + job.getTitle() + " has been "+ status +  " by " +
                    user.getFirstName() + " " + user.getLastName();

            emailSenderService.sendEmail(email, subject, message);
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
