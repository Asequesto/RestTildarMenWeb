package kz.tildarmen.TildarMen.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "work_experience")
public class WorkExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String position;
    private String companyName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String description;

    @ManyToOne
    @JoinColumn(name = "translator_id")
    private Translator translator;

}
