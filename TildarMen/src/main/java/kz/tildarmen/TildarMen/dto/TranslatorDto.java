package kz.tildarmen.TildarMen.dto;

import kz.tildarmen.TildarMen.enums.AvailabilityStatus;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class TranslatorDto {


    private String introduction;
    private String professionalTitle;
    private double rating;
    private int reviews;
    private AvailabilityStatus availability;
    private Set<LanguageDto> languages;
    private Set<ServiceTypesDto> serviceTypes;
    private Set<SpecializationDto> specializations;
    private LocationDto location;
    private VideoDto videoGreeting;
    private List<ImageDto> images;
    private List<WorkExperienceDto> workExperiences;
    private List<EducationDto> educations;
    private List<CertificateDto> certificates;

}
