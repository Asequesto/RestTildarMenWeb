package kz.tildarmen.TildarMen.dto;

import kz.tildarmen.TildarMen.enums.RequestStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class JobTranslatorsDto {

    private String firstName;
    private String lastName;
    private RequestStatus status;
    private LocalDate sendAt;
    private Long price;
    private double rating;

}
