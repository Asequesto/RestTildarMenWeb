package kz.tildarmen.TildarMen.services;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.dto.LanguageDto;
import kz.tildarmen.TildarMen.mapper.LanguageMapper;
import kz.tildarmen.TildarMen.model.Language;
import kz.tildarmen.TildarMen.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class LanguageService {

    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;

    public Language getLanguageByName(String language) {
        return languageRepository.findByName(language);
    }

    public List<Language> getAllByName(List<String> languages) {
        return languageRepository.findByNameIn(languages);
    }

    public List<LanguageDto> getAllLanguages() {
        List<Language> languages = languageRepository.findAll();
        return languageMapper.toDtoList(languages);
    }
}
