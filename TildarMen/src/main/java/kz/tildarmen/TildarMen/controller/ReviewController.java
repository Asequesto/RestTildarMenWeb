package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.dto.ReviewDto;
import kz.tildarmen.TildarMen.mapper.ReviewMapper;
import kz.tildarmen.TildarMen.model.Review;
import kz.tildarmen.TildarMen.model.User;
import kz.tildarmen.TildarMen.requests.CreateReviewRequest;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.AuthService;
import kz.tildarmen.TildarMen.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;
    private final AuthService authService;

    @GetMapping("/{id}/get")
    public ResponseEntity<ApiResponse> getReview(@PathVariable Long id) {
        try {
            Review review = reviewService.getReviewById(id);
            return ResponseEntity.ok(new ApiResponse("Success", reviewMapper.toDto(review)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/{translatorId}/all")
    public ResponseEntity<ApiResponse> getAllTranslatorReviews(@PathVariable Long translatorId) {
        try {
            List<ReviewDto> reviews = reviewService.getAllTranslatorReview(translatorId);
            return ResponseEntity.ok(new ApiResponse("Success", reviews));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{userId}/create/{translatorId}")
    public ResponseEntity<ApiResponse> createReview(@RequestBody CreateReviewRequest request,
                                                    @PathVariable Long translatorId,
                                                    @PathVariable Long userId,
                                                    @AuthenticationPrincipal User userDetails) {
        try {
            if (translatorId.equals(userDetails.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse("Forbidden", "You do not have permission"));
            }
            ReviewDto review = reviewService.createTranslatorReview(translatorId, userId, request);
            return ResponseEntity.ok(new ApiResponse("Success", review));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("{id}/delete")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable Long id,
                                                    @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            reviewService.deleteTranslatorReview(id);
            return ResponseEntity.ok(new ApiResponse("Success", null));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse("Error", e.getMessage()));
        }
    }
}
