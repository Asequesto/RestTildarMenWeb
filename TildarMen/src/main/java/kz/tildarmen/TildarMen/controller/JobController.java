package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.dto.JobDto;
import kz.tildarmen.TildarMen.requests.SearchJobsRequest;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;


    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllJobs() {
        List<JobDto> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(new ApiResponse("Success", jobs));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getJobsByTitle(@RequestParam String title) {
        List<JobDto> jobs = jobService.searchJobsByTitle(title);
        return ResponseEntity.ok(new ApiResponse("Success", jobs));
    }

    @PostMapping("/filter")
    public ResponseEntity<ApiResponse> getFilteredJobs(@RequestParam(required = false) String availability,
                                                              @RequestBody(required = false) SearchJobsRequest request) {

        List<JobDto> jobs = jobService
                .filterJobs(availability, request);
        return ResponseEntity.ok(new ApiResponse("Success", jobs));
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYER')")
    @PostMapping("/{employerId}/add")
    public ResponseEntity<ApiResponse> addJob(@PathVariable Long employerId, @RequestBody JobDto job) {
        try {
            JobDto newJob = jobService.addJob(employerId, job);
            return ResponseEntity.ok(new ApiResponse("Success", newJob));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYER')")
    @PutMapping("/{employerId}/update/{jobId}")
    public ResponseEntity<ApiResponse> updateJob(@PathVariable Long employerId,
                                                 @PathVariable Long jobId,
                                                 @RequestBody JobDto job) {
        try {
            JobDto newJob = jobService.updateJobById(employerId, jobId, job);
            return ResponseEntity.ok(new ApiResponse("Success", newJob));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYER')")
    @DeleteMapping("/{employerId}/delete/{jobId}")
    public ResponseEntity<ApiResponse> deleteJob(@PathVariable Long employerId, @PathVariable Long jobId) {
        try {
            jobService.deleteJobByEmployerId(employerId, jobId);
            return ResponseEntity.ok(new ApiResponse("Delete Success", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

}
