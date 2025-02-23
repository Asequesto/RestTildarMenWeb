package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.WorkExperienceDto;
import kz.tildarmen.TildarMen.model.WorkExperience;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkExperienceMapper {


    WorkExperienceDto toDto(WorkExperience workExperience);

    @InheritInverseConfiguration
    WorkExperience toEntity(WorkExperienceDto workExperienceDto);

    List<WorkExperienceDto> toDtoList(List<WorkExperience> workExperiences);
    List<WorkExperience> toEntityList(List<WorkExperienceDto> workExperienceDtoList);

}
