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
    private String profileImageUrl;
    private String videoUrl;
    private AvailabilityStatus availability;
    private ImageDto image;
    private List<String> projectUrls;
    private Set<LanguageDto> languages;
    private Set<ServiceTypesDto> serviceTypes;
    private Set<SpecializationDto> specializations;
    private LocationDto location;
    private List<WorkExperienceDto> workExperiences;
    private List<EducationDto> educations;
    private List<CertificateDto> certificates;

}
