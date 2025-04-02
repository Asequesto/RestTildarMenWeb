package kz.tildarmen.TildarMen.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class JobDto {

    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate publicationDate;
    private Long price;
    private int applicantsCount;
    Set<LocationDto> locations;
    Set<LanguageDto> languages;
    Set<ServiceTypesDto> serviceTypes;
    Set<SpecializationDto> specializations;

}
