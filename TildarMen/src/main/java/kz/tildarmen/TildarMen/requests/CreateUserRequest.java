package kz.tildarmen.TildarMen.requests;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private Integer code;
    private String phoneNumber;
    private String password;
    private String confirmPassword;
    private String role;
}
