package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.LocationDto;
import kz.tildarmen.TildarMen.model.Location;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    LocationDto toDto(Location location);

    @InheritInverseConfiguration
    Location toEntity(LocationDto locationDto);

    List<LocationDto> toDtoList(List<Location> cities);
}
