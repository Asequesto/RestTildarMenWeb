package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.TranslatorSettingsDto;
import kz.tildarmen.TildarMen.model.Location;
import kz.tildarmen.TildarMen.model.Translator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TranslatorSettingsMapper {


    @Mapping(target = "location", source = "location")
    TranslatorSettingsDto toDto(Translator translator);

    default String map(Location location) {
        if (location == null) return null;
        return location.getCity();
    }

}
