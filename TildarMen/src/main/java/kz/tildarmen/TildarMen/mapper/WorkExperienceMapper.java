package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.WorkExperienceDto;
import kz.tildarmen.TildarMen.model.WorkExperience;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkExperienceMapper {

    @Mapping(target = "companyName", source = "companyName")
    @Mapping(target = "position", source = "position")
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "endDate", source = "endDate")
    @Mapping(target = "description", source = "description")
    WorkExperienceDto toDto(WorkExperience workExperience);

    @InheritInverseConfiguration
    WorkExperience toEntity(WorkExperienceDto workExperienceDto);

    List<WorkExperienceDto> toDtoList(List<WorkExperience> workExperiences);
    List<WorkExperience> toEntityList(List<WorkExperienceDto> workExperienceDtoList);

}
