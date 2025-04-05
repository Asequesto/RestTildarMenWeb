package kz.tildarmen.TildarMen.services;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.dto.LocationDto;
import kz.tildarmen.TildarMen.mapper.LocationMapper;
import kz.tildarmen.TildarMen.repository.LocationRepository;
import kz.tildarmen.TildarMen.model.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;


    public Location getLocationByName(String location) {
        return locationRepository.getLocationsByCity(location);
    }

    public List<Location> getAllByName(List<String> locations) {
        return locationRepository.findByCityInIgnoreCase(locations);
    }

    public List<LocationDto> getAllCities() {
        List<Location> cities = locationRepository.findAll();
        return locationMapper.toDtoList(cities);
    }
}
