package kz.tildarmen.TildarMen.controller;

import jakarta.mail.MessagingException;
import kz.tildarmen.TildarMen.chatroom.ChatRoom;
import kz.tildarmen.TildarMen.chatroom.ChatRoomRepository;
import kz.tildarmen.TildarMen.dto.SearchTranslatorDto;
import kz.tildarmen.TildarMen.dto.TranslatorDto;
import kz.tildarmen.TildarMen.dto.UserDto;
import kz.tildarmen.TildarMen.mapper.TranslatorMapper;
import kz.tildarmen.TildarMen.mapper.UserMapper;
import kz.tildarmen.TildarMen.model.EmailVerifyToken;
import kz.tildarmen.TildarMen.model.User;
import kz.tildarmen.TildarMen.repository.EmailVerifyTokenRepository;
import kz.tildarmen.TildarMen.requests.*;
import kz.tildarmen.TildarMen.response.ApiResponse;
import kz.tildarmen.TildarMen.services.EmployerService;
import kz.tildarmen.TildarMen.services.TranslatorService;
import kz.tildarmen.TildarMen.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final TranslatorService translatorService;
    private final EmployerService employerService;
    private final UserMapper userMapper;
    private final TranslatorMapper translatorMapper;
    private final EmailVerifyTokenRepository emailVerifyTokenRepository;
    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(new ApiResponse("Success", userMapper.toUserDto(user)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("User not found, Fail!", e.getMessage()));
        }
    }

    @GetMapping("/translator/{id}/profile")
    public ResponseEntity<ApiResponse> getTranslatorProfile(@PathVariable Long id) {
        try {
            TranslatorDto translator = translatorMapper.toDto(translatorService.getTranslatorById(id));
            return ResponseEntity.ok(new ApiResponse("Success", translator));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/employers/search")
    public ResponseEntity<ApiResponse> searchEmployers(@RequestParam String username) {
        List<UserDto> users = employerService.searchEmployersByName(username);
        return ResponseEntity.ok(new ApiResponse("Success", users));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addUser(@RequestBody CreateUserRequest request) {
        UserDto user;
        EmailVerifyToken token = emailVerifyTokenRepository.findByEmail(request.getEmail());
        if(token == null || token.getToken() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Email verify token not found, Fail!", request.getEmail()));
        }
        if(!(token.getToken().equals(request.getCode()))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Email verify code is incorrect!", request.getEmail()));
        }
        Date now = new Date();
        if(!(now.before(token.getExpiratyDate()))){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Email verify expired!", request.getEmail()));
        }
        if(request.getPhoneNumber() == null || request.getPhoneNumber().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Phone number is empty or null!", "phone: " + request.getPhoneNumber()));
        }

        if(request.getPassword() == null || request.getPassword().isEmpty() ||
                (!request.getPassword().equals(request.getConfirmPassword()))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ApiResponse("Passwords do not match!", "Enter a valid password!"));
        }

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
        emailVerifyTokenRepository.delete(token);
        return ResponseEntity.ok(new ApiResponse("Success", user));
    }

    @PostMapping("/translators/filter")
    public ResponseEntity<ApiResponse> getFilteredTranslators(@RequestParam(required = false) String username,
                                                              @RequestBody(required = false) SearchTranslatorsRequest request) {

        List<SearchTranslatorDto> translators = translatorService
                .filterTranslators(username, request);
        return ResponseEntity.ok(new ApiResponse("Success", translators));
    }

    @PostMapping("/send-val")
    public ResponseEntity<ApiResponse> sendValCode(@RequestPart String email){
        try {
            userService.sendVerificationEmail(email);
            return ResponseEntity.ok(new ApiResponse("Success", null));
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PostMapping("/send-code")
    public ResponseEntity<ApiResponse> sendCode(@RequestPart String email) {
        try {
            userService.sendResetEmail(email);
            return ResponseEntity.ok(new ApiResponse("Success", null));
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error", e.getMessage()));
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/chats")
    public ResponseEntity<ApiResponse> getChats(@AuthenticationPrincipal User user) {
        List<ChatRoom> rooms = chatRoomRepository.findAllBySenderIdOrRecipientId(
                user.getId().toString(), user.getId().toString());
        List<Long> userIds = rooms.stream()
                .filter(room -> !room.getSenderId().equals(room.getRecipientId()))
                .map(room -> {
                    String sender = room.getSenderId();
                    String recipient = room.getRecipientId();
                    return sender.equals(user.getId().toString()) ? Long.parseLong(recipient) : Long.parseLong(sender);
                })
                .distinct()
                .toList();
        List<User> users = userService.findByIdIn(userIds);
        return ResponseEntity.ok(new ApiResponse("Success", userMapper.toDtoList(users)));
    }




}
