package kz.tildarmen.TildarMen.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToMany
    @JoinTable(
            name = "job_locations",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    Set<Location> locations;

    @ManyToMany
    @JoinTable(
            name = "job_languages",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    Set<Language> languages;

    @ManyToMany
    @JoinTable(
            name = "job_service_types",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private Set<ServiceTypes> serviceTypes;

    @ManyToMany
    @JoinTable(
            name = "job_specializations",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id")
    )
    private Set<Specialization> specializations;

    @ManyToOne
    @JoinColumn(name = "employer_id") // Foreign key in Job table
    private Employer employer;

}
