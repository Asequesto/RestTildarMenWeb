package kz.tildarmen.TildarMen.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String comment;
    private String profileImageUrl;
    private int rating;
    private LocalDateTime creationDate;

}
