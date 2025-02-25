package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.JobDto;
import kz.tildarmen.TildarMen.model.Job;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobMapper {

    JobDto toDto(Job job);

    @InheritInverseConfiguration
    Job toEntity(JobDto dto);

    List<JobDto> toDtoList(List<Job> jobs);
}
