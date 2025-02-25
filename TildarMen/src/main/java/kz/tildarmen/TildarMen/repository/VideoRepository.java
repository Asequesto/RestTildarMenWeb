package kz.tildarmen.TildarMen.repository;

import kz.tildarmen.TildarMen.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
}
