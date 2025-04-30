package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.model.Employer;
import kz.tildarmen.TildarMen.model.Location;
import kz.tildarmen.TildarMen.requests.GetEmployerProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GetEmployerProfileMapper {

    GetEmployerProfile toDto(Employer employer);

    default String map(Location location) {
        if (location == null) return null;
        return location.getCity();
    }

}
