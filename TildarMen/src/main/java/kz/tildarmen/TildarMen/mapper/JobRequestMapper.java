package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.JobRequestDto;
import kz.tildarmen.TildarMen.model.JobRequest;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobRequestMapper {

    JobRequestDto toDto(JobRequest jobRequest);

    @InheritInverseConfiguration
    JobRequest toEntity(JobRequestDto jobRequestDto);

}
