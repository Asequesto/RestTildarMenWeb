package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.SearchTranslatorDto;
import kz.tildarmen.TildarMen.model.Translator;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SearchTranslatorMapper {

    SearchTranslatorDto toDto(Translator translator);

    List<SearchTranslatorDto> toDtoList(List<Translator> translators);

}
