package kz.tildarmen.TildarMen.requests;

import lombok.Data;

@Data
public class UpdatePasswordRequest {

    private String oldPassword;
    private String password;
    private String repeatPassword;


}
