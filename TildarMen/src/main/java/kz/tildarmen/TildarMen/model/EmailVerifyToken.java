package kz.tildarmen.TildarMen.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class EmailVerifyToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;

    private Integer token;
    private Date sendAt;
    private Date expiratyDate;

}
