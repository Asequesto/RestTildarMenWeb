package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.model.Employer;
import kz.tildarmen.TildarMen.requests.GetEmployerProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GetEmployerProfileMapper {

    @Mapping(source = "location.city", target = "location")
    GetEmployerProfile toDto(Employer employer);
}
