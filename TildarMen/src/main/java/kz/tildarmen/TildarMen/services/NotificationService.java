package kz.tildarmen.TildarMen.services;

import kz.tildarmen.TildarMen.dto.NotificationDto;
import kz.tildarmen.TildarMen.enums.NotificationType;
import kz.tildarmen.TildarMen.mapper.NotificationMapper;
import kz.tildarmen.TildarMen.model.Notification;
import kz.tildarmen.TildarMen.model.User;
import kz.tildarmen.TildarMen.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public void sendNotification(User user, String title, String message, String profileImage, NotificationType type) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setProfileImageUrl(profileImage);
        notification.setType(type);
        notification.setUser(user);
        notification.setSendAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    public List<NotificationDto> getAllUserNotifications(User user) {
        return notificationMapper.toDtoList(notificationRepository.getAllByUser(user));
    }

    public NotificationDto getById(Long id) {
        Notification notification = notificationRepository.getReferenceById(id);
        notification.setRead(true);
        notificationRepository.save(notification);
        return notificationMapper.toDto(notification);
    }
}
