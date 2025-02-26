package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.TranslatorDto;
import kz.tildarmen.TildarMen.model.Translator;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TranslatorMapper {


    TranslatorDto toDto(Translator translator);

    @InheritInverseConfiguration
    Translator toEntity(TranslatorDto translatorDto);

    List<TranslatorDto> toDtoList(List<Translator> translators);


}
