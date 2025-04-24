package kz.tildarmen.TildarMen.controller;

import jakarta.mail.MessagingException;
import kz.tildarmen.TildarMen.dto.SearchTranslatorDto;
import kz.tildarmen.TildarMen.dto.UserDto;
import kz.tildarmen.TildarMen.mapper.UserMapper;
import kz.tildarmen.TildarMen.model.User;
import kz.tildarmen.TildarMen.requests.CreateUserRequest;
import kz.tildarmen.TildarMen.requests.ResetPasswordRequest;
import kz.tildarmen.TildarMen.requests.SearchTranslatorsRequest;
import kz.tildarmen.TildarMen.requests.VerifyCodeRequest;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.EmployerService;
import kz.tildarmen.TildarMen.services.TranslatorService;
import kz.tildarmen.TildarMen.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final TranslatorService translatorService;
    private final EmployerService employerService;
    private final UserMapper userMapper;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(new ApiResponse("Success", userMapper.toUserDto(user)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("User not found, Fail!", e.getMessage()));
        }
    }

    @GetMapping("/translators/search")
    public ResponseEntity<ApiResponse> searchTranslators(@RequestParam String username) {
        List<SearchTranslatorDto> translators = translatorService.searchTranslatorsByName(username);
        return ResponseEntity.ok(new ApiResponse("Success", translators));
    }

    @GetMapping("/employers/search")
    public ResponseEntity<ApiResponse> searchEmployers(@RequestParam String username) {
        List<UserDto> users = employerService.searchEmployersByName(username);
        return ResponseEntity.ok(new ApiResponse("Success", users));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addUser(@RequestBody CreateUserRequest request) {
        UserDto user;
        if (request.getRole().equalsIgnoreCase("translator")) {
            user = translatorService.createTranslator(request);
        } else if (request.getRole().equalsIgnoreCase("employer")) {
            user = employerService.createEmployer(request);
        }
        else{
            user = userService.createUser(request);
        }
        if(user == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("User Already Exists", null));
        }
        return ResponseEntity.ok(new ApiResponse("Success", user));
    }

    @PostMapping("/translators/filter")
    public ResponseEntity<ApiResponse> getFilteredTranslators(@RequestParam(required = false) String availability,
                                                              @RequestBody(required = false) SearchTranslatorsRequest request) {

        List<SearchTranslatorDto> translators = translatorService
                .filterTranslators(availability, request);
        return ResponseEntity.ok(new ApiResponse("Success", translators));
    }

    @PostMapping("/send-code")
    public ResponseEntity<ApiResponse> sendCode(@RequestParam String email) {
        try {
            userService.sendVerificationEmail(email);
            return ResponseEntity.ok(new ApiResponse("Success", null));
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<ApiResponse> verifyCode(@RequestBody VerifyCodeRequest request) {
        try {
            String res = userService.verifyCode(request.getCode(), request.getEmail());
            if(res.equals("Success")) {
                return ResponseEntity.ok(new ApiResponse(res, null));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error", res));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            userService.resetPassword(request);
            return ResponseEntity.ok(new ApiResponse("Success", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }




}
