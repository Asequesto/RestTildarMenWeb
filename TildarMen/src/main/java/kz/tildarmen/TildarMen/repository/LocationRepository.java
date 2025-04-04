package kz.tildarmen.TildarMen.repository;

import kz.tildarmen.TildarMen.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Location getLocationsByCity(String location);

    List<Location> findByCityInIgnoreCase(List<String> locations);
}
