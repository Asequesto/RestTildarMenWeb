package kz.tildarmen.TildarMen.repository;

import kz.tildarmen.TildarMen.model.StripeAccount;
import kz.tildarmen.TildarMen.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StripeAccountRepository extends JpaRepository<StripeAccount, Long> {
    StripeAccount findByUser(User user);

    Optional<StripeAccount> findByStripeId(String id);
}
