package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.dto.JobDto;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    @PostMapping("/{employerId}/add")
    public ResponseEntity<ApiResponse> addJob(@PathVariable Long employerId, @RequestBody JobDto job) {
        try {
            JobDto newJob = jobService.addJob(employerId, job);
            return ResponseEntity.ok(new ApiResponse("Success", newJob));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

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
