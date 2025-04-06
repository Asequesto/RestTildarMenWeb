package kz.tildarmen.TildarMen.repository;

import kz.tildarmen.TildarMen.model.StripeAccount;
import kz.tildarmen.TildarMen.model.Translator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StripeAccountRepository extends JpaRepository<StripeAccount, Long> {
    StripeAccount findByUser(Translator translator);
}
