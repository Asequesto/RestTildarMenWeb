package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.ServiceTypesDto;
import kz.tildarmen.TildarMen.model.ServiceTypes;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ServiceTypesMapper {

    ServiceTypesDto toDto(ServiceTypes serviceTypes);

    @InheritInverseConfiguration
    ServiceTypes toEntity(ServiceTypesDto serviceTypesDto);

    Set<ServiceTypes> toEntitySet(Set<ServiceTypesDto> serviceTypesDtoSet);

    List<ServiceTypesDto> toDtoList(List<ServiceTypes> serviceTypes);
}
