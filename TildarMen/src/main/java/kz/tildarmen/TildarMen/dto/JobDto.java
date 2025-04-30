package kz.tildarmen.TildarMen.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class JobDto {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime publicationDate;
    private Long price;
    private int applicantsCount;
    String location;
    Set<LanguageDto> languages;
    Set<ServiceTypesDto> serviceTypes;
    Set<SpecializationDto> specializations;

}
