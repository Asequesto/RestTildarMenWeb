package kz.tildarmen.TildarMen.config;

import kz.tildarmen.TildarMen.model.Specialization;
import kz.tildarmen.TildarMen.repository.SpecializationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SpecializationSeeder implements CommandLineRunner {

    private final SpecializationRepository specializationRepository;


    @Override
    public void run(String... args) {
        if (specializationRepository.count() == 0) { // Prevent duplicate seeding
            List<Specialization> specializations = List.of(
                    new Specialization(null, "Legal Translation"),
                    new Specialization(null, "Medical Translation"),
                    new Specialization(null, "Technical Translation"),
                    new Specialization(null, "Financial Translation"),
                    new Specialization(null, "Marketing Translation"),
                    new Specialization(null, "Literary Translation"),
                    new Specialization(null, "Scientific Translation"),
                    new Specialization(null, "Software Localization"),
                    new Specialization(null, "Website Localization"),
                    new Specialization(null, "Diplomatic Translation")
            );

            specializationRepository.saveAll(specializations);
            System.out.println("✅ Specializations Seeded Successfully!");
        } else {
            System.out.println("✅ Specializations already exist. Skipping seeding.");
        }
    }
}

