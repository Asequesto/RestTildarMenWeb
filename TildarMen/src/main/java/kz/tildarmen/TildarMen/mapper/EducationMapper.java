package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.EducationDto;
import kz.tildarmen.TildarMen.model.Education;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EducationMapper {


    EducationDto toDto(Education education);

    @InheritInverseConfiguration
    Education toEntity(EducationDto educationDto);

    List<EducationDto> toDtoList(List<Education> educations);

}
