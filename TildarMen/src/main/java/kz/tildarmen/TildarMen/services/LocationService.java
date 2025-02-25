package kz.tildarmen.TildarMen.services;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.mapper.LocationRepository;
import kz.tildarmen.TildarMen.model.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class LocationService {

    private final LocationRepository locationRepository;


    public Location getLocationByName(String location) {

        return locationRepository.getLocationsByCity(location);
    }
}
