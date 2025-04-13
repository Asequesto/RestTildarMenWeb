package kz.tildarmen.TildarMen.dto;

import lombok.Data;

import java.util.Set;

@Data
public class SearchTranslatorDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String profileImageUrl;
    private LocationDto location;

    private Set<LanguageDto> languages;
    private Set<ServiceTypesDto> serviceTypes;
    private Set<SpecializationDto> specializations;

}
