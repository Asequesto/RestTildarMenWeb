package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.TranslatorDto;
import kz.tildarmen.TildarMen.model.Translator;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TranslatorMapper {


    TranslatorDto toDto(Translator translator);

    @InheritInverseConfiguration
    Translator toEntityList(TranslatorDto translatorDto);

}
