package kz.tildarmen.TildarMen.model;

import jakarta.persistence.*;
import kz.tildarmen.TildarMen.enums.ImageUsageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;

    @Enumerated(EnumType.STRING)
    private ImageUsageType usageType;

    @Lob
    private Blob image;
    private String downloadUrl;

    @ManyToOne
    @JoinColumn(name = "translator_id")
    private Translator translator;

}
