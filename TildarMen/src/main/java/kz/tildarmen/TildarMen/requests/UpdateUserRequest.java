package kz.tildarmen.TildarMen.requests;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String location;

}
