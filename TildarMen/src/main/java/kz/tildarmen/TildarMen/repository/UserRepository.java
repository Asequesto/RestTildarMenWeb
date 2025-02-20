package kz.tildarmen.TildarMen.repository;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByPhoneNumber(String phoneNumber);

    User findByEmailOrPhoneNumber(String email, String phoneNumber);
}
