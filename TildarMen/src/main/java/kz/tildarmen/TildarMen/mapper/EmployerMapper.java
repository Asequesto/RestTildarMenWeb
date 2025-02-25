package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.EmployerDto;
import kz.tildarmen.TildarMen.model.Employer;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployerMapper {

    EmployerDto toDto(Employer employer);

    @InheritInverseConfiguration
    Employer toEntity(EmployerDto employerDto);

}
