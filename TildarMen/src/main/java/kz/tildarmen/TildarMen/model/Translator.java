package kz.tildarmen.TildarMen.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "translators")
public class Translator extends User {

    private String introduction;
    private String videoGreeting;

    @ElementCollection
    private List<String> languages;

    @ElementCollection
    private List<String> serviceTypes;

    @ElementCollection
    private List<String> specializations;

    @OneToMany(mappedBy = "translator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkExperience> workExperiences;

    @OneToMany(mappedBy = "translator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects;

    @OneToMany(mappedBy = "translator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educations;

    @OneToMany(mappedBy = "translator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Certificate> certificates;

}
