package kz.tildarmen.TildarMen.repository;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.enums.ImageUsageType;
import kz.tildarmen.TildarMen.model.Image;
import kz.tildarmen.TildarMen.model.Translator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface ImageRepository extends JpaRepository<Image, Long> {
    boolean existsByTranslatorAndUsageType(Translator translator, ImageUsageType usageType);

    Image findByTranslator(Translator translator);
}
