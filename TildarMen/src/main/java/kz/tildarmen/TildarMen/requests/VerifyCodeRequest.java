package kz.tildarmen.TildarMen.requests;

import lombok.Data;

@Data
public class VerifyCodeRequest {

    String email;
    Integer code;

}
