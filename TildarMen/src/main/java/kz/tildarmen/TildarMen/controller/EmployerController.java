package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.dto.JobApplicationDto;
import kz.tildarmen.TildarMen.dto.JobDto;
import kz.tildarmen.TildarMen.dto.JobRequestDto;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.EmployerService;
import kz.tildarmen.TildarMen.services.JobApplicationService;
import kz.tildarmen.TildarMen.services.JobRequestService;
import kz.tildarmen.TildarMen.services.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/employer")
@PreAuthorize("hasAnyAuthority('EMPLOYER')")
public class EmployerController {

    private final JobService jobService;
    private final EmployerService employerService;
    private final JobApplicationService jobApplicationService;
    private final JobRequestService jobRequestService;

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
            Set<JobRequestDto> requests = jobRequestService.getEmployerRequests(id);
            return ResponseEntity.ok(new ApiResponse("Success", requests));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/job/{id}/applications")
    public ResponseEntity<ApiResponse> getAllEmployerJobApplications(@PathVariable Long id) {
        try {
            Set<JobApplicationDto> requests = jobApplicationService.getEmployerApplications(id);
            return ResponseEntity.ok(new ApiResponse("Success", requests));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
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
