package kz.tildarmen.TildarMen.config;

import kz.tildarmen.TildarMen.model.ServiceTypes;
import kz.tildarmen.TildarMen.repository.ServiceTypesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ServiceTypesSeeder implements CommandLineRunner {

    private final ServiceTypesRepository serviceTypesRepository;

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
            System.out.println("✅ Service types seeded successfully.");
        } else {
            System.out.println("✅ Service types are already saved.");
        }
    }
}

