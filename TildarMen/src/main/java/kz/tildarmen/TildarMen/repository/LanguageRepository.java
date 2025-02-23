package kz.tildarmen.TildarMen.repository;

import kz.tildarmen.TildarMen.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    Language findByName(String language);
}
