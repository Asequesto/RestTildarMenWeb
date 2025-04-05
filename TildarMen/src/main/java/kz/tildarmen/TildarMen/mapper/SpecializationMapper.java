package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.SpecializationDto;
import kz.tildarmen.TildarMen.model.Specialization;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface SpecializationMapper {

    SpecializationDto toDto(Specialization specialization);

    @InheritInverseConfiguration
    Specialization toEntity(SpecializationDto specializationDto);

    Set<Specialization> toEntitySet(Set<SpecializationDto> specializationDtoSet);

    List<SpecializationDto> toDtoList(List<Specialization> specializations);
}
