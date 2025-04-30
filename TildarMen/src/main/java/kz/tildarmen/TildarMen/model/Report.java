package kz.tildarmen.TildarMen.model;


import jakarta.persistence.*;
import kz.tildarmen.TildarMen.enums.ReportReason;
import kz.tildarmen.TildarMen.enums.ReportType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ReportType type;
    private ReportReason reason;
    private String title;
    private String details;
    private String fileUrl;
    private LocalDateTime createdAt;

    @ManyToOne
    private User reporter;

    @ManyToOne
    private Job reportedJob;

    @ManyToOne
    private Translator reportedTranslator;

}
