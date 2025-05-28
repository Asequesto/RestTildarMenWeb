package kz.tildarmen.TildarMen.repository;

import kz.tildarmen.TildarMen.model.Notification;
import kz.tildarmen.TildarMen.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> getAllByUserOrderBySendAtDesc(User user);
}
