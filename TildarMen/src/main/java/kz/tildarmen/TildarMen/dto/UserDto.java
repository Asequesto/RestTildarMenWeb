package kz.tildarmen.TildarMen.dto;

import kz.tildarmen.TildarMen.enums.Role;
import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Role role;
}
