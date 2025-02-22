package kz.tildarmen.TildarMen.dto;

import kz.tildarmen.TildarMen.enums.AvailabilityStatus;
import kz.tildarmen.TildarMen.model.Education;
import kz.tildarmen.TildarMen.model.Project;
import lombok.Data;

import java.util.List;

@Data
public class TranslatorDto {

    private String introduction;
    private String videoGreeting;
    private String professionalTitle;
    private AvailabilityStatus availability;
    private List<String> languages;
    private List<String> serviceTypes;
    private List<String> specializations;
    private List<WorkExperienceDto> workExperiences;
    private List<Project> projects;
    private List<Education> educations;
    private List<CertificateDto> certificates;

}
