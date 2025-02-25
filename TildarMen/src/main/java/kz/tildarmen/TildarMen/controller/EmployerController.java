package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.dto.JobDto;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.EmployerService;
import kz.tildarmen.TildarMen.services.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/employer")
public class EmployerController {

    private final JobService jobService;
    private final EmployerService employerService;

    @GetMapping("/{id}/jobs")
    public ResponseEntity<ApiResponse> getAllEmployerJobs(@PathVariable Long id) {
        try {
            List<JobDto> jobs = jobService.getJobsByEmployerId(id);
            return ResponseEntity.ok(new ApiResponse("Success", jobs));
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
