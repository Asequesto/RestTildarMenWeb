package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.JobApplicationDto;
import kz.tildarmen.TildarMen.model.JobApplication;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobApplicationMapper {

    JobApplicationDto toDto(JobApplication jobApplication);

    @InheritInverseConfiguration
    JobApplication toEntity(JobApplicationDto jobApplicationDto);

}
