package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.TranslatorSettingsDto;
import kz.tildarmen.TildarMen.model.Translator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TranslatorSettingsMapper {

    @Mapping(source = "location.city", target = "location")
    TranslatorSettingsDto toDto(Translator translator);

}
