package kz.tildarmen.TildarMen.dto;

import kz.tildarmen.TildarMen.enums.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDto {

    private Long id;
    private String title;
    private String message;
    private LocalDateTime sendAt;
    private boolean isRead;
    private NotificationType type;

}
