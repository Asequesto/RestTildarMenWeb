package kz.tildarmen.TildarMen.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WorkExperienceDto {

    private Long id;
    private String position;
    private String companyName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String description;


}
