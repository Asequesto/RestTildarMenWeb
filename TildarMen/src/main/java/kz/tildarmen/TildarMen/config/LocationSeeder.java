package kz.tildarmen.TildarMen.config;

import kz.tildarmen.TildarMen.mapper.LocationRepository;
import kz.tildarmen.TildarMen.model.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Component
public class LocationSeeder implements CommandLineRunner {

    private final LocationRepository locationRepository;

    @Override
    public void run(String... args) {
        if (locationRepository.count() == 0) {
            List<Location> cities = Arrays.asList(
                    new Location(null, "Almaty"),
                    new Location(null, "Astana"),
                    new Location(null, "Shymkent"),
                    new Location(null, "Aktobe"),
                    new Location(null, "Karaganda"),
                    new Location(null, "Taraz"),
                    new Location(null, "Pavlodar"),
                    new Location(null, "Oskemen"),
                    new Location(null, "Semey"),
                    new Location(null, "Atyrau"),
                    new Location(null, "Aktau"),
                    new Location(null, "Kostanay"),
                    new Location(null, "Kyzylorda"),
                    new Location(null, "Oral"),
                    new Location(null, "Petropavl"),
                    new Location(null, "Taldykorgan"),
                    new Location(null, "Ekibastuz"),
                    new Location(null, "Turkestan"),
                    new Location(null, "Kokshetau"),
                    new Location(null, "Zhanaozen"),
                    new Location(null, "Rudny")
            );

            locationRepository.saveAll(cities);
        }
    }
}
