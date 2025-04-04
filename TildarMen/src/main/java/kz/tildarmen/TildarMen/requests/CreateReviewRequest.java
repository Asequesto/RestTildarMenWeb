package kz.tildarmen.TildarMen.requests;

import lombok.Data;

@Data
public class CreateReviewRequest {
    private String comment;
    private int rating;
}
