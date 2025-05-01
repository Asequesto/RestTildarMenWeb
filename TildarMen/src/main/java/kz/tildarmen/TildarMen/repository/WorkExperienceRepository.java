package kz.tildarmen.TildarMen.repository;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.model.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long> {
}
