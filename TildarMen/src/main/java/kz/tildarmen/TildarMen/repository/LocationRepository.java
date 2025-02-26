package kz.tildarmen.TildarMen.repository;

import kz.tildarmen.TildarMen.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Location getLocationsByCity(String location);

    List<Location> findByCityInIgnoreCase(List<String> locations);
}
