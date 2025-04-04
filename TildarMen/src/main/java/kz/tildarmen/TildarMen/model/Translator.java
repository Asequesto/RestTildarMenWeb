package kz.tildarmen.TildarMen.model;

import jakarta.persistence.*;
import kz.tildarmen.TildarMen.enums.AvailabilityStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "translators")
public class Translator extends User {


    @Column(columnDefinition = "TEXT")
    private String introduction;

    private String professionalTitle;

    private double rating = 5.0;
    private int reviews = 0;

    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availability;

    @ManyToMany
    @JoinTable(
            name = "translator_languages",
            joinColumns = @JoinColumn(name = "translator_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private Set<Language> languages;

    @ManyToMany
    @JoinTable(
            name = "translator_service_types",
            joinColumns = @JoinColumn(name = "translator_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private Set<ServiceTypes> serviceTypes;

    @ManyToMany
    @JoinTable(
            name = "translator_specializations",
            joinColumns = @JoinColumn(name = "translator_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id")
    )
    private Set<Specialization> specializations;

    @ManyToOne
    private Location location;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Video videoGreeting;

    @OneToMany(mappedBy = "translator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "translator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkExperience> workExperiences;

    @OneToMany(mappedBy = "translator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educations;

    @OneToMany(mappedBy = "translator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Certificate> certificates;

    @OneToMany(mappedBy = "translator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobRequest> jobRequests = new ArrayList<>();

    @OneToMany(mappedBy = "translator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobApplication> applications = new ArrayList<>();

    public void addRating(double rating) {
        double totalRating = this.rating * this.reviews;
        this.reviews++;
        totalRating += rating;
        this.rating = totalRating / this.reviews;
    }

    public void removeRating(double rating) {
        double totalRating = this.rating * this.reviews;
        this.reviews--;
        totalRating -= rating;
        this.rating = totalRating / this.reviews;
    }

}
