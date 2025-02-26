package kz.tildarmen.TildarMen.repository;

import kz.tildarmen.TildarMen.model.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
    Specialization findByName(String name);

    List<Specialization> findByNameIn(List<String> specializations);
}
