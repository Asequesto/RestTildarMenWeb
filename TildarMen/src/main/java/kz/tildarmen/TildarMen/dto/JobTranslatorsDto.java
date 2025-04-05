package kz.tildarmen.TildarMen.dto;

import kz.tildarmen.TildarMen.enums.RequestStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobTranslatorsDto {

    private String firstName;
    private String lastName;
    private RequestStatus status;
    private LocalDateTime sendAt;
    private Long price;
    private double rating;

}
