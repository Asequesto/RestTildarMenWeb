package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.LocationDto;
import kz.tildarmen.TildarMen.model.Location;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    LocationDto toDto(Location location);

    @InheritInverseConfiguration
    Location toEntity(LocationDto locationDto);

}
