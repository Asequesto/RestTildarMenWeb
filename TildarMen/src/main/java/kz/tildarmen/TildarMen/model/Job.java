package kz.tildarmen.TildarMen.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime publicationDate = LocalDateTime.now();
    private Long price;
    private int applicantsCount = 0;

    public void increaseApplicantsCount() {
        this.applicantsCount++;
    }

    public void decreaseApplicantsCount() {
        if (this.applicantsCount > 0) this.applicantsCount--;
    }

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobRequest> jobRequests = new ArrayList<>();

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobApplication> applications = new ArrayList<>();

    @OneToMany(mappedBy = "reportedJob", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "job_languages",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private Set<Language> languages;

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
    @JoinColumn(name = "employer_id")
    private Employer employer;



}
