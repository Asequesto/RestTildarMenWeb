package kz.tildarmen.TildarMen.config;

import kz.tildarmen.TildarMen.model.ServiceTypes;
import kz.tildarmen.TildarMen.repository.ServiceTypesRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import org.slf4j.Logger;

@Component
@RequiredArgsConstructor
public class ServiceTypesSeeder implements CommandLineRunner {

    private final ServiceTypesRepository serviceTypesRepository;
    private static final Logger logger = LoggerFactory.getLogger(ServiceTypesSeeder.class);

    @Override
    public void run(String... args) {
        if (serviceTypesRepository.count() == 0) { // Prevent duplicate seeding
            List<ServiceTypes> services = List.of(
                    new ServiceTypes(null, "Book Translation"),
                    new ServiceTypes(null, "Real-time Interpretation"),
                    new ServiceTypes(null, "Video Game Localization"),
                    new ServiceTypes(null, "Movie & TV Subtitle Translation"),
                    new ServiceTypes(null, "Website Localization"),
                    new ServiceTypes(null, "Software Localization"),
                    new ServiceTypes(null, "Medical Translation"),
                    new ServiceTypes(null, "Legal Document Translation"),
                    new ServiceTypes(null, "Financial Document Translation"),
                    new ServiceTypes(null, "Marketing & Advertising Translation")
            );

            serviceTypesRepository.saveAll(services);
            logger.info("✅ Service types seeded successfully.");
        } else {
            logger.info("✅ Service types are already saved.");
        }
    }
}

