package kz.tildarmen.TildarMen.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateEducationRequest {

    private String degree;
    private String university;
    private Long graduationYear;
    private MultipartFile degreeFile;


}
