package kz.tildarmen.TildarMen.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class WorkExperienceDto {

    private String position;
    private String companyName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;


}
