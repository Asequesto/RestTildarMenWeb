package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.dto.ReportDto;
import kz.tildarmen.TildarMen.enums.ReportReason;
import kz.tildarmen.TildarMen.model.User;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/report")
public class ReportController {


    private final ReportService reportService;


    @GetMapping("/report-reason")
    public ResponseEntity<ApiResponse> reportReasonDropdown(){
        return ResponseEntity.ok(new ApiResponse("Success", Arrays.stream(ReportReason.values())
                .map(Enum::name)
                .collect(Collectors.toList())));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/translator/{id}")
    public ResponseEntity<ApiResponse> reportTranslator(@AuthenticationPrincipal User userDetails,
                                                        @PathVariable Long id,
                                                        @RequestPart ReportDto reportDto,
                                                        @RequestParam(required = false) MultipartFile file) {
        try {
            ReportDto report = reportService.reportTranslator(id, userDetails.getId(), reportDto, file);
            return ResponseEntity.ok(new ApiResponse("Success", report));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Internal Server Error", e.getMessage()));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Not Found", e.getMessage()));
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/job/{id}")
    public ResponseEntity<ApiResponse> reportJob(@AuthenticationPrincipal User userDetails,
                                                        @PathVariable Long id,
                                                        @RequestPart ReportDto reportDto,
                                                        @RequestParam(required = false) MultipartFile file ) {
        try {
            ReportDto report = reportService.reportJob(id, userDetails.getId(), reportDto, file);
            return ResponseEntity.ok(new ApiResponse("Success", report));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Internal Server Error", e.getMessage()));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Not Found", e.getMessage()));
        }
    }

}
