package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.dto.JobApplicationDto;
import kz.tildarmen.TildarMen.model.Job;
import kz.tildarmen.TildarMen.model.JobApplication;
import kz.tildarmen.TildarMen.model.User;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.EmailSenderService;
import kz.tildarmen.TildarMen.services.JobApplicationService;
import kz.tildarmen.TildarMen.services.UserService;
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
    private final UserService userService;
    private final EmailSenderService emailSenderService;

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


    @PreAuthorize("hasAnyAuthority('EMPLOYER')")
    @PutMapping("/{applicationId}/application")
    public ResponseEntity<ApiResponse> responseJobApplication(@PathVariable Long applicationId, @RequestParam String status) {
        try {
            jobApplicationService.updateApplicationStatus(applicationId, status);

            JobApplication application = jobApplicationService.findById(applicationId);
            User user = userService.getAuthenticatedUser();
            Job job = application.getJob();

            String email = application.getTranslator().getEmail();
            String subject = "Job Application got responded";
            String message = "Your job application for - " + job.getTitle() + " has been "+ status +  " by " +
                    user.getFirstName() + " " + user.getLastName();

            emailSenderService.sendEmail(email, subject, message);
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
