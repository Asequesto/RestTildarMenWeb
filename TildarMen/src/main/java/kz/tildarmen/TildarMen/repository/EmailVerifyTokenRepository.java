package kz.tildarmen.TildarMen.repository;
import kz.tildarmen.TildarMen.model.EmailVerifyToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerifyTokenRepository extends JpaRepository<EmailVerifyToken, Long> {
    EmailVerifyToken findByEmail(String email);
}
