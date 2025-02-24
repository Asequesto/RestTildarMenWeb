package kz.tildarmen.TildarMen.repository;

import kz.tildarmen.TildarMen.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
}
