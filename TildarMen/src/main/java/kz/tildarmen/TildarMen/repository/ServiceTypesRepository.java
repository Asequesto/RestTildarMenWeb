package kz.tildarmen.TildarMen.repository;

import kz.tildarmen.TildarMen.model.ServiceTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceTypesRepository extends JpaRepository<ServiceTypes, Long> {
    ServiceTypes findByName(String name);

    List<ServiceTypes> findByNameIn(List<String> names);
}
