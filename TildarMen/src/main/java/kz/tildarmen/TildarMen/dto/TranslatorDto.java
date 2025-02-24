package kz.tildarmen.TildarMen.dto;

import kz.tildarmen.TildarMen.enums.AvailabilityStatus;
import kz.tildarmen.TildarMen.model.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class TranslatorDto {


    private String introduction;
    private String videoGreeting;
    private String professionalTitle;
    private String basedIn;
    private AvailabilityStatus availability;
    private Set<Language> languages;
    private Set<ServiceTypes> serviceTypes;
    private Set<Specialization> specializations;
    private List<ImageDto> profileImage;
    private List<WorkExperienceDto> workExperiences;
    private List<Project> projects;
    private List<Education> educations;
    private List<CertificateDto> certificates;

}
