package kz.tildarmen.TildarMen.requests;

import lombok.Data;

@Data
public class GetEmployerProfile {
    String firstName;
    String lastName;
    String profileImageUrl;
    String location;
    String introduction;
}
