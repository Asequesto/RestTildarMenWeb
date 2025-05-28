package kz.tildarmen.TildarMen.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import kz.tildarmen.TildarMen.enums.NotificationType;
import kz.tildarmen.TildarMen.model.*;
import kz.tildarmen.TildarMen.repository.StripeAccountRepository;
import kz.tildarmen.TildarMen.repository.TransactionRepository;
import kz.tildarmen.TildarMen.requests.StripeAccCreateRequest;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stripe")
public class StripeController {

    private final StripeService stripeService;
    private final TranslatorService translatorService;
    private final JobService jobService;
    private final TransactionRepository transactionRepository;
    private final AuthService authService;
    private final NotificationService notificationService;
    private final StripeAccountRepository stripeAccountRepository;


    @PreAuthorize("hasAnyAuthority('TRANSLATOR')")
    @GetMapping("/account/{id}")
    public ResponseEntity<ApiResponse> getAccount(@PathVariable Long id, @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            return ResponseEntity.ok(new ApiResponse("Success", stripeService.getAccount(id)));
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error", e.getMessage()));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }

    }

    @PreAuthorize("hasAnyAuthority('TRANSLATOR')")
    @PostMapping("/account/{id}")
    public ResponseEntity<ApiResponse> createStripeAccount(@PathVariable Long id,
                                                           @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            StripeAccCreateRequest request = stripeService.createAccount(id);
            return ResponseEntity.ok(new ApiResponse("Success", request));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error", e.getMessage()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/payment/{id}/job/{jobId}")
    public ResponseEntity<ApiResponse> payment(@PathVariable Long id, @PathVariable Long jobId,
                                               @AuthenticationPrincipal User userDetails) {
        try {
            authService.checkPermission(userDetails, id);
            return ResponseEntity.ok(new ApiResponse("Success", stripeService.payment(id, jobId)));
        } catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("Forbidden", e.getMessage()));
        }
        catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error", e.getMessage()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PostMapping("/webhook/onboarding")
    public ResponseEntity<ApiResponse> webhookOnboarding(HttpServletRequest request) {
        String payload;
        String sigHeader = request.getHeader("Stripe-Signature");
        String endpointSecret = "whsec_o8dBCKKLQBnm80zoa1PIZWD4ljz83pya";

        try (Scanner s = new Scanner(request.getInputStream()).useDelimiter("\\A")) {
            payload = s.hasNext() ? s.next() : "";
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Failed to read payload", e.getMessage()));
        }

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Invalid signature", e.getMessage()));
        }

        if ("account.updated".equals(event.getType())) {
            var deserializer = event.getDataObjectDeserializer();
            if (deserializer.getObject().isPresent()) {
                Account account = (Account) deserializer.getObject().get();
                if (account.getChargesEnabled() && account.getDetailsSubmitted()) {
                    System.out.println("Onboarding completed for account: " + account.getId());
                    StripeAccount stripeAccount = new StripeAccount();
                    Translator translator = translatorService.
                            getTranslatorById(Long.valueOf(account.getMetadata().get("translatorId")));
                    stripeAccount.setUser(translator);
                    stripeAccount.setStripeId(account.getId());
                    stripeAccountRepository.save(stripeAccount);
                }
            }
        }

        return ResponseEntity.ok(new ApiResponse("Success", null));
    }

    @PostMapping("/webhook")
    public ResponseEntity<ApiResponse> handleStripeWebhook(@RequestBody String payload,
                                                           @RequestHeader("Stripe-Signature") String sigHeader){
        String endpointSecret = "whsec_zcoXe1XabirKDyFl24JVguJzOPfMnojD";
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error", e.getMessage()));
        }

        if("checkout.session.completed".equals(event.getType())){
            EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();

            if(deserializer.getObject().isPresent()){
                Session session = (Session) deserializer.getObject().get();

                String translatorId = session.getClientReferenceId();
                Long jobId = Long.valueOf(session.getMetadata().get("jobId"));
                Translator translator = translatorService.getTranslatorById(Long.parseLong(translatorId));
                Job job = jobService.getJobById(jobId);

                Transaction transaction = new Transaction();
                transaction.setTranslator(translator);
                transaction.setDate(LocalDateTime.now());
                transaction.setDescription(job.getTitle());
                transaction.setEmployer(job.getEmployer());
                transaction.setPrice(job.getPrice());

                notificationService.sendNotification(job.getEmployer(),
                        "Payment Successful.",
                        "Your payment of + " + job.getPrice() + "₸ has been successfully transferred.",
                        null,
                        NotificationType.PAYMENT_SENT);
                notificationService.sendNotification(translator, "Payment Received",
                        "You've received a " + job.getPrice() + "₸ payment for the project " +
                        job.getTitle(), job.getEmployer().getProfileImageUrl(), NotificationType.PAYMENT_RECEIVED);

                transactionRepository.save(transaction);
            } else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("Error", event.getType()));
            }
        }
        return ResponseEntity.ok(new ApiResponse("Webhook success", null));
    }

}
