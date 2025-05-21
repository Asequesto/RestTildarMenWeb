package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.model.User;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.AuthService;
import kz.tildarmen.TildarMen.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
@PreAuthorize("isAuthenticated()")
public class NotificationController {

    private final NotificationService notificationService;
    private final AuthService authService;

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse> getAllNotifications(@AuthenticationPrincipal User user) {
        try {
            return ResponseEntity.ok(new ApiResponse("Success", notificationService.getAllUserNotifications(user)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/{userId}/get/{id}")
    public ResponseEntity<ApiResponse> getNotificationById(@AuthenticationPrincipal User user,
                                                           @PathVariable Long userId,
                                                           @PathVariable Long id) {
        try {
            authService.checkPermission(user, userId);
            return ResponseEntity.ok(new ApiResponse("Success", notificationService.getById(id)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error", e.getMessage()));
        }
    }

}
