package kz.tildarmen.TildarMen.repository;
import kz.tildarmen.TildarMen.model.EmailVerifyToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface EmailVerifyTokenRepository extends JpaRepository<EmailVerifyToken, Long> {
    EmailVerifyToken findByEmail(String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM EmailVerifyToken t WHERE t.expiratyDate < :now")
    void deleteAllExpiredTokens(LocalDateTime now);
}
