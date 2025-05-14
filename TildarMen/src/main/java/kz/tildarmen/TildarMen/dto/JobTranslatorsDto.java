package kz.tildarmen.TildarMen.dto;

import kz.tildarmen.TildarMen.enums.RequestStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobTranslatorsDto {

    private String applicationId;
    private String type;
    private String firstName;
    private String lastName;
    private String profileImageUrl;
    private RequestStatus status;
    private LocalDateTime sendAt;
    private Long price;
    private double rating;

}
