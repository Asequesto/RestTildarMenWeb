package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.JobApplicationDto;
import kz.tildarmen.TildarMen.dto.JobDescDto;
import kz.tildarmen.TildarMen.model.Job;
import kz.tildarmen.TildarMen.model.JobApplication;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobApplicationMapper {

    JobDescDto toJobDescDto(Job job);

    JobApplicationDto toDto(JobApplication jobApplication);

    @InheritInverseConfiguration
    JobApplication toEntity(JobApplicationDto jobApplicationDto);

    List<JobApplicationDto> toDtoList(List<JobApplication> applications);
}
