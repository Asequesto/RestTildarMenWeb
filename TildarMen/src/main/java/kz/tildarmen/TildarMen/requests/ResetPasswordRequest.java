package kz.tildarmen.TildarMen.requests;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    String email;
    String password;
    String confirmPassword;

}
