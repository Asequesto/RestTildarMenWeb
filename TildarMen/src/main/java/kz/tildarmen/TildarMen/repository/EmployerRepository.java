package kz.tildarmen.TildarMen.repository;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.model.Employer;
import kz.tildarmen.TildarMen.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface EmployerRepository extends JpaRepository<Employer, Long> {

    @Query("SELECT e FROM Employer e " +
            "WHERE LOWER(e.firstName) " +
            "LIKE LOWER(CONCAT('%', :username, '%')) " +
            "OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :username, '%'))")
    List<User> searchEmployerByUsername(String username);
}
