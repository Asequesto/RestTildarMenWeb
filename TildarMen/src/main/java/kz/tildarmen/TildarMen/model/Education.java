package kz.tildarmen.TildarMen.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "education")
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String degree;
    private String university;
    private Long graduationYear;
    private String degreeFileUrl;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "translator_id")
    private Translator translator;

}

