package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.LanguageDto;
import kz.tildarmen.TildarMen.model.Language;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface LanguageMapper {

    LanguageDto toDto(Language language);

    @InheritInverseConfiguration
    Language toEntity(LanguageDto languageDto);

    Set<Language> toEntitySet(Set<LanguageDto> languageDtoSet);

}
