package kz.tildarmen.TildarMen.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
@DiscriminatorValue("TRANSLATOR")
public class Translator extends User {

    @ElementCollection
    private List<String> languages;

    @ElementCollection
    private List<String> certificates;

    @ElementCollection
    private List<String> experience;

    private Double rating = 0.0; // Default rating



    public void updateProfile(List<String> languages, List<String> certificates, List<String> experience) {
        this.languages = languages;
        this.certificates = certificates;
        this.experience = experience;
    }
}