package kz.tildarmen.TildarMen.model;

import jakarta.persistence.*;
import kz.tildarmen.TildarMen.enums.RequestStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class JobRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    private LocalDateTime requestedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "translator_id", nullable = false)
    private Translator translator;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "employer_id", nullable = false)
    private Employer employer;

}
