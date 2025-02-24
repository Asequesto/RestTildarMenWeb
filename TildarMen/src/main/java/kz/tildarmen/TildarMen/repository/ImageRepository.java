package kz.tildarmen.TildarMen.repository;

import kz.tildarmen.TildarMen.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
