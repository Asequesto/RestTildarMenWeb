package kz.tildarmen.TildarMen.requests;

import lombok.Data;

@Data
public class UploadEducationRequest {

    private String degree;
    private String university;
    private Long graduationYear;

}
