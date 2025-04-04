package kz.tildarmen.TildarMen.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReviewDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String comment;
    private int rating;
    private LocalDate creationDate;

}
