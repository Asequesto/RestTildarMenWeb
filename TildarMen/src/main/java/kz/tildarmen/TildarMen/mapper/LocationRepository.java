package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Location getLocationsByCity(String location);
}
