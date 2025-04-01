package kz.tildarmen.TildarMen.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Employer extends User{

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @ManyToOne
    private Location location;

    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Job> jobs = new ArrayList<>();

    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JobRequest> jobRequests = new HashSet<>();

}
