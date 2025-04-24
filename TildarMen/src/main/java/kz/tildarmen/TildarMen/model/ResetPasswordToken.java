package kz.tildarmen.TildarMen.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class ResetPasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer token;
    private Date sendAt;
    private Date expiryDate;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

}
