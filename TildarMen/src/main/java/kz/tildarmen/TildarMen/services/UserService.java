package kz.tildarmen.TildarMen.services;


import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.dto.UserDto;
import kz.tildarmen.TildarMen.enums.Role;
import kz.tildarmen.TildarMen.mapper.UserMapper;
import kz.tildarmen.TildarMen.model.EmailVerifyToken;
import kz.tildarmen.TildarMen.model.ResetPasswordToken;
import kz.tildarmen.TildarMen.model.User;
import kz.tildarmen.TildarMen.repository.EmailVerifyTokenRepository;
import kz.tildarmen.TildarMen.repository.ResetPasswordTokenRepository;
import kz.tildarmen.TildarMen.repository.UserRepository;
import kz.tildarmen.TildarMen.requests.CreateUserRequest;
import kz.tildarmen.TildarMen.requests.ResetPasswordRequest;
import kz.tildarmen.TildarMen.requests.UpdatePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmailSenderService emailSenderService;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final EmailVerifyTokenRepository emailVerifyTokenRepository;

    public User getUserById(Long id){

        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserDto createUser(CreateUserRequest request) {
        User checkUser = findUserByUserName(request.getEmail(), request.getPhoneNumber()    );
        if(checkUser != null){
            return null;
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(Role.valueOf(request.getRole().toUpperCase()));

        return userMapper.toUserDto(userRepository.save(user));

    }

    public User findUserByUserName(String email, String phoneNumber){
        return userRepository.findByEmailOrPhoneNumber(email, phoneNumber);
    }

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User findUserByPhoneNumber(String phoneNumber){
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }

    public void checkPassword(UpdatePasswordRequest request, User user) {
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        if(!request.getPassword().equals(request.getRepeatPassword())) {
            throw new IllegalArgumentException("Passwords does not match");
        }
    }

    public void sendVerificationEmail(String email) throws MessagingException {
        User user = findUserByEmail(email);
        if(user != null){
            throw new RuntimeException("User already exists");
        }
        Integer verificationCode = (int) (Math.random() * 900_000) + 100_000;
        EmailVerifyToken emailVerifyToken = emailVerifyTokenRepository.findByEmail(email);
        if(emailVerifyToken != null){
            emailVerifyToken.setToken(verificationCode);
            Date expiryDate = new Date(System.currentTimeMillis() + 20 * 60 * 1000);
            Date now = new Date();
            emailVerifyToken.setExpiratyDate(expiryDate);
            emailVerifyToken.setSendAt(now);
        }
        else {
            emailVerifyToken = new EmailVerifyToken();
            emailVerifyToken.setEmail(email);
            emailVerifyToken.setToken(verificationCode);
            Date expiryDate = new Date(System.currentTimeMillis() + 20 * 60 * 1000);
            Date now = new Date();
            emailVerifyToken.setExpiratyDate(expiryDate);
            emailVerifyToken.setSendAt(now);
        }
        emailVerifyTokenRepository.save(emailVerifyToken);

        emailSenderService.sendEmail(
                email,
                "Your verification code",
                """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Your Verification Code</title>
                    <style>
                        * {
                            box-sizing: border-box;
                        }
                        .container {
                            max-width: 600px;
                            width: 100%%;
                            margin: auto;
                            background-color: #ffffff;
                            padding: 30px;
                            border-radius: 10px;
                            font-family: Arial, sans-serif;
                            box-shadow: 0 0 10px rgba(0,0,0,0.1);
                        }
                        .code {
                            font-size: 32px;
                            letter-spacing: 8px;
                            color: #4CAF50;
                            background-color: #f0f0f0;
                            padding: 15px 30px;
                            border-radius: 8px;
                            text-align: center;
                            font-weight: bold;
                            margin: 20px 0;
                        }
                        .footer {
                            margin-top: 30px;
                            font-size: 12px;
                            color: #888;
                            text-align: center;
                        }
                    </style>
                </head>
                <body style="background-color: #f4f4f4; padding: 40px; margin: 0;">
                    <div class="container">
                        <h2>Hello from TildarMen 👋</h2>
                        <p>Use the code below to validate your email:</p>
                        <div class="code">%s</div>
                        <p>This code will expire in 15 minutes.</p>
                        <p>If you didn't request this, you can ignore this email.</p>
                        <div class="footer">
                            &copy; 2025 TildarMen. All rights reserved.
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(verificationCode)
        );

    }

    public void sendResetEmail(String email) throws MessagingException {
        User user = userRepository.findByEmail(email);
        Integer verificationCode = (int) (Math.random() * 900_000) + 100_000;
        if(user == null){
            throw new RuntimeException("User not found");
        }
        ResetPasswordToken token = resetPasswordTokenRepository.findByUser(user);
        if(token != null){
            token.setToken(verificationCode);
            Date expiryDate = new Date(System.currentTimeMillis() + 20 * 60 * 1000);
            Date now = new Date();
            token.setSendAt(now);
            token.setExpiryDate(expiryDate);
        }
        else {
            token = new ResetPasswordToken();
            token.setToken(verificationCode);
            token.setUser(user);
            Date expiryDate = new Date(System.currentTimeMillis() + 20 * 60 * 1000);
            Date now = new Date();
            token.setSendAt(now);
            token.setExpiryDate(expiryDate);
        }
        resetPasswordTokenRepository.save(token);
        emailSenderService.sendEmail(
                email,
                "Your verification code",
                """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Your Verification Code</title>
                    <style>
                        * {
                            box-sizing: border-box;
                        }
                        .container {
                            max-width: 600px;
                            width: 100%%;
                            margin: auto;
                            background-color: #ffffff;
                            padding: 30px;
                            border-radius: 10px;
                            font-family: Arial, sans-serif;
                            box-shadow: 0 0 10px rgba(0,0,0,0.1);
                        }
                        .code {
                            font-size: 32px;
                            letter-spacing: 8px;
                            color: #4CAF50;
                            background-color: #f0f0f0;
                            padding: 15px 30px;
                            border-radius: 8px;
                            text-align: center;
                            font-weight: bold;
                            margin: 20px 0;
                        }
                        .footer {
                            margin-top: 30px;
                            font-size: 12px;
                            color: #888;
                            text-align: center;
                        }
                    </style>
                </head>
                <body style="background-color: #f4f4f4; padding: 40px; margin: 0;">
                    <div class="container">
                        <h2>Hello from TildarMen 👋</h2>
                        <p>Use the code below to reset your password:</p>
                        <div class="code">%s</div>
                        <p>This code will expire in 15 minutes.</p>
                        <p>If you didn't request this, you can ignore this email.</p>
                        <div class="footer">
                            &copy; 2025 TildarMen. All rights reserved.
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(verificationCode)
        );

    }

    public void deleteToken(String email){
        User user = userRepository.findByEmail(email);
        resetPasswordTokenRepository.deleteByUser(user);
    }

    public void resetPassword(ResetPasswordRequest request){
        User user = userRepository.findByEmail(request.getEmail());
        if(user == null){
            throw new RuntimeException("User not found");
        }
        if(!request.getPassword().equals(request.getConfirmPassword())){
            throw new RuntimeException("Passwords do not match");
        }
        ResetPasswordToken token = resetPasswordTokenRepository.findByUser(user);
        if(token == null){
            throw new RuntimeException("Token not found");
        }
        Date now = new Date();
        if(!now.before(token.getExpiryDate())){
            throw new RuntimeException("Token expired");
        }
        if(!request.getCode().equals(token.getToken())){
            throw new RuntimeException("Invalid verification code");
        }
        deleteToken(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

    }

    public List<User> findByIdIn(List<Long> userIds) {
        return userRepository.findByIdIn(userIds);
    }
}
