package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.dto.JobApplicationDto;
import kz.tildarmen.TildarMen.dto.JobDto;
import kz.tildarmen.TildarMen.dto.JobRequestDto;
import kz.tildarmen.TildarMen.dto.JobTranslatorsDto;
import kz.tildarmen.TildarMen.model.Employer;
import kz.tildarmen.TildarMen.requests.GetEmployerProfile;
import kz.tildarmen.TildarMen.requests.UpdatePasswordRequest;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/employer")
@PreAuthorize("hasAnyAuthority('EMPLOYER')")
public class EmployerController {

    private final JobService jobService;
    private final EmployerService employerService;
    private final JobApplicationService jobApplicationService;
    private final JobRequestService jobRequestService;
    private final ImageService imageService;

    @GetMapping("/{id}/profile")
    public ResponseEntity<ApiResponse> getEmployerProfile(@PathVariable Long id) {
        try {
            Employer employer = employerService.getEmployerById(id);
            GetEmployerProfile profile = new GetEmployerProfile();
            profile.setIntroduction(employer.getIntroduction());
            profile.setFirstName(employer.getFirstName());
            profile.setLastName(employer.getLastName());
            if(employer.getLocation() != null) profile.setLocation(employer.getLocation().getCity());
            return ResponseEntity.ok(new ApiResponse("Success", profile));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/jobs")
    public ResponseEntity<ApiResponse> getAllEmployerJobs(@PathVariable Long id) {
        try {
            List<JobDto> jobs = jobService.getJobsByEmployerId(id);
            return ResponseEntity.ok(new ApiResponse("Success", jobs));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/requests")
    public ResponseEntity<ApiResponse> getAllEmployerRequests(@PathVariable Long id) {
        try {
            List<JobRequestDto> requests = jobRequestService.getEmployerRequests(id);
            return ResponseEntity.ok(new ApiResponse("Success", requests));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/job/{id}/applications")
    public ResponseEntity<ApiResponse> getAllEmployerJobApplications(@PathVariable Long id) {
        try {
            List<JobApplicationDto> requests = jobApplicationService.getEmployerApplications(id);
            return ResponseEntity.ok(new ApiResponse("Success", requests));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/job/{id}/applicants")
    public ResponseEntity<ApiResponse> getAllJobTranslators(@PathVariable Long id) {
        try {
            List<JobTranslatorsDto> translators = jobService.getJobTranslators(id);
            return ResponseEntity.ok(new ApiResponse("Success", translators));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/profile-image")
    public ResponseEntity<ApiResponse> uploadProfileImage(@PathVariable Long id,
                                                          @RequestParam MultipartFile file) {

        try {
            Employer employer = employerService.getEmployerById(id);
            if(employer.getProfileImageUrl() != null){
                imageService.deleteImage(employer.getProfileImageUrl());
                employer.setProfileImageUrl(null);
            }
            String url = imageService.uploadFileEmployer(id, file);
            return ResponseEntity.ok(new ApiResponse("Success", url));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/profile/update")
    public ResponseEntity<ApiResponse> updateEmployerProfile(@PathVariable Long id,
                                                          @RequestBody GetEmployerProfile profile) {
        try {
            employerService.updateProfile(profile, id);
            return ResponseEntity.ok(new ApiResponse("Success", profile));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponse> updateEmployerPassword(@PathVariable Long id,
                                                              @RequestBody UpdatePasswordRequest request){
        try {
            employerService.updatePassword(id, request);
            return ResponseEntity.ok(new ApiResponse("Success", request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Oops", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse> deleteEmployerById(@PathVariable Long id) {
        try {
            employerService.deleteEmployerById(id);
            return ResponseEntity.ok(new ApiResponse("Success", "Employer deleted"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }
}
