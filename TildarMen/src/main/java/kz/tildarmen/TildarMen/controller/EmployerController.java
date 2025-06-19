package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.chatroom.ChatRoomService;
import kz.tildarmen.TildarMen.dto.JobApplicationDto;
import kz.tildarmen.TildarMen.dto.JobDto;
import kz.tildarmen.TildarMen.dto.JobRequestDto;
import kz.tildarmen.TildarMen.dto.JobTranslatorsDto;
import kz.tildarmen.TildarMen.mapper.GetEmployerProfileMapper;
import kz.tildarmen.TildarMen.model.Employer;
import kz.tildarmen.TildarMen.model.Job;
import kz.tildarmen.TildarMen.model.User;
import kz.tildarmen.TildarMen.requests.GetEmployerProfile;
import kz.tildarmen.TildarMen.requests.UpdatePasswordRequest;
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
@RequestMapping("/employer")
@PreAuthorize("hasAnyAuthority('EMPLOYER')")
public class EmployerController {

    private final JobService jobService;
    private final EmployerService employerService;
    private final JobApplicationService jobApplicationService;
    private final JobRequestService jobRequestService;
    private final ImageService imageService;
    private final TransactionService transactionService;
    private final GetEmployerProfileMapper getEmployerProfileMapper;
    private final AuthService authService;
    private final ChatRoomService chatRoomService;

    @GetMapping("/{id}/profile")
    public ResponseEntity<ApiResponse> getEmployerProfile(@PathVariable Long id,
                                                          @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            Employer employer = employerService.getEmployerById(id);
            return ResponseEntity.ok(new ApiResponse("Success", getEmployerProfileMapper.toDto(employer)));
        }  catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}/jobs")
    public ResponseEntity<ApiResponse> getAllEmployerJobs(@PathVariable Long id,
                                                          @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            List<JobDto> jobs = jobService.getJobsByEmployerId(id);
            return ResponseEntity.ok(new ApiResponse("Success", jobs));
        }  catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/requests")
    public ResponseEntity<ApiResponse> getAllEmployerRequests(@PathVariable Long id,
                                                              @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            List<JobRequestDto> requests = jobRequestService.getEmployerRequests(id);
            return ResponseEntity.ok(new ApiResponse("Success", requests));
        }  catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/job/{id}/applications")
    public ResponseEntity<ApiResponse> getAllEmployerJobApplications(@PathVariable Long id,
                                                                     @AuthenticationPrincipal User userDetails) {
        try {
            Job job = jobService.getJobById(id);
            authService.checkPermission(userDetails, job.getEmployer().getId());
            List<JobApplicationDto> requests = jobApplicationService.getEmployerApplications(id);
            return ResponseEntity.ok(new ApiResponse("Success", requests));
        }  catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/job/{id}/applicants")
    public ResponseEntity<ApiResponse> getAllJobTranslators(@PathVariable Long id,
                                                            @AuthenticationPrincipal User userDetails) {
        try {
            Job job = jobService.getJobById(id);
            authService.checkPermission(userDetails, job.getEmployer().getId());
            List<JobTranslatorsDto> translators = jobService.getJobTranslators(id);
            return ResponseEntity.ok(new ApiResponse("Success", translators));
        }  catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<ApiResponse> getAllEmployerTransactions(@PathVariable Long id,
                                                                  @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            return ResponseEntity.ok(new ApiResponse("Success", transactionService.getAllTransaction(id)));
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
            Employer employer = employerService.getEmployerById(id);
            if(employer.getProfileImageUrl() != null){
                imageService.deleteImage(employer.getProfileImageUrl());
                employer.setProfileImageUrl(null);
            }
            String url = imageService.uploadFileEmployer(id, file);
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

    @PutMapping("/{id}/profile/update")
    public ResponseEntity<ApiResponse> updateEmployerProfile(@PathVariable Long id,
                                                             @RequestBody GetEmployerProfile profile,
                                                             @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            employerService.updateProfile(profile, id);
            return ResponseEntity.ok(new ApiResponse("Success", profile));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponse> updateEmployerPassword(@PathVariable Long id,
                                                              @RequestBody UpdatePasswordRequest request,
                                                              @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            employerService.updatePassword(id, request);
            return ResponseEntity.ok(new ApiResponse("Success", request));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Oops", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse> deleteEmployerById(@PathVariable Long id,
                                                          @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            employerService.deleteEmployerById(id);
            chatRoomService.deleteAllUserChatRooms(String.valueOf(id));
            return ResponseEntity.ok(new ApiResponse("Success", "Employer deleted"));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }
}
