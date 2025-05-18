package kz.tildarmen.TildarMen.dto;

import lombok.Data;

import java.util.Set;

@Data
public class Job8Dto {

    private Long id;
    private String title;
    private String description;
    private String employerProfilePicture;
    String location;
    Set<LanguageDto> languages;
    Set<ServiceTypesDto> serviceTypes;
    Set<SpecializationDto> specializations;

}
