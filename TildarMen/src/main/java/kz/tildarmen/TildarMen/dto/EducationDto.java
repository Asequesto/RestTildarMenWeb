package kz.tildarmen.TildarMen.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class EducationDto {

    private String degree;
    private String university;
    private Long graduationYear;
    private MultipartFile degreeFile;

}
