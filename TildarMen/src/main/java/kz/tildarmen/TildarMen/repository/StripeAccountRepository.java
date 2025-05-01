package kz.tildarmen.TildarMen.repository;

import kz.tildarmen.TildarMen.model.StripeAccount;
import kz.tildarmen.TildarMen.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StripeAccountRepository extends JpaRepository<StripeAccount, Long> {
    StripeAccount findByUser(User user);
}
