package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.dto.UserDto;
import kz.tildarmen.TildarMen.requests.CreateUserRequest;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.TranslatorService;
import kz.tildarmen.TildarMen.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final TranslatorService translatorService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            UserDto user = userService.getUserById(userId);
            return ResponseEntity.ok(new ApiResponse("Success", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("User not found, Fail!", e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addUser(@RequestBody CreateUserRequest request) {
        UserDto user;
        if (request.getRole().equalsIgnoreCase("translator")) {
            user = translatorService.createTranslator(request);
        } else {
            user = userService.createUser(request);
        }
        if(user == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("User Already Exists", null));
        }
        return ResponseEntity.ok(new ApiResponse("Success", user));
    }



}
