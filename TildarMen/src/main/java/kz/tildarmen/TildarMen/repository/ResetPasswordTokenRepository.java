package kz.tildarmen.TildarMen.repository;

import kz.tildarmen.TildarMen.model.ResetPasswordToken;
import kz.tildarmen.TildarMen.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {
    ResetPasswordToken findByUser(User user);

    @Modifying
    @Query("DELETE FROM ResetPasswordToken t WHERE t.user = :user")
    void deleteByUser(@Param("user") User user);
}
