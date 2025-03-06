package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.JobApplicationDto;
import kz.tildarmen.TildarMen.model.JobApplication;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface JobApplicationMapper {

    JobApplicationDto toDto(JobApplication jobApplication);

    @InheritInverseConfiguration
    JobApplication toEntity(JobApplicationDto jobApplicationDto);

    Set<JobApplicationDto> toDtoSet(Set<JobApplication> applications);
}
