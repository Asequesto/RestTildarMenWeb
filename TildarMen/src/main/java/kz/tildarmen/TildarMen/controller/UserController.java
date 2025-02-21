package kz.tildarmen.TildarMen.controller;

import kz.tildarmen.TildarMen.dto.UserDto;
import kz.tildarmen.TildarMen.mapper.UserMapper;
import kz.tildarmen.TildarMen.model.User;
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
    private final UserMapper userMapper;
    private final TranslatorService translatorService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            UserDto userDto = userMapper.toUserDto(user);
            return ResponseEntity.ok(new ApiResponse("Success", userDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("User not found, Fail!", e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addUser(@RequestBody CreateUserRequest request) {
        User user;
        if (request.getRole().equalsIgnoreCase("translator")) {
            user = translatorService.createTranslator(request);
        } else {
            user = userService.createUser(request);
        }
        UserDto userDto = userMapper.toUserDto(user);
        if(user == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("User Already Exists", null));
        }
        System.out.println("DEBUG UserDto: " + userDto);
        return ResponseEntity.ok(new ApiResponse("Success", userDto));
    }



}
