package kz.tildarmen.TildarMen.model;

import jakarta.persistence.*;
import kz.tildarmen.TildarMen.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String message;
    private String profileImageUrl;
    private LocalDateTime sendAt;
    private boolean isRead;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @ManyToOne
    private User user;
}
