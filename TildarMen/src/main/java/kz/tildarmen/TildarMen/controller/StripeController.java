package kz.tildarmen.TildarMen.controller;

import com.stripe.exception.StripeException;
import kz.tildarmen.TildarMen.requests.StripeAccCreateRequest;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.StripeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stripe")
public class StripeController {

    private final StripeService stripeService;


    @PreAuthorize("hasAnyAuthority('TRANSLATOR')")
    @GetMapping("/account/{id}")
    public ResponseEntity<ApiResponse> getAccount(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new ApiResponse("Success", stripeService.getAccount(id)));
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error", e.getMessage()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }

    }

    @PreAuthorize("hasAnyAuthority('TRANSLATOR')")
    @PostMapping("/account/{id}")
    public ResponseEntity<ApiResponse> createStripeAccount(@PathVariable Long id) {
        try {
            StripeAccCreateRequest request = stripeService.createAccount(id);
            return ResponseEntity.ok(new ApiResponse("Success", request));
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error", e.getMessage()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/payment/{id}/job/{jobId}")
    public ResponseEntity<ApiResponse> payment(@PathVariable Long id, @PathVariable Long jobId) {
        try {
            return ResponseEntity.ok(new ApiResponse("Success", stripeService.payment(id, jobId)));
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error", e.getMessage()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

}
