package kz.tildarmen.TildarMen.dto;

import lombok.Data;

@Data
public class EducationDto {

    private Long id;
    private String degree;
    private String university;
    private Long graduationYear;
    private String degreeFileUrl;

}
