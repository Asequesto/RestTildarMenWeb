package kz.tildarmen.TildarMen.repository;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.enums.AvailabilityStatus;
import kz.tildarmen.TildarMen.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Transactional
@Repository
public interface TranslatorRepository extends JpaRepository<Translator, Long> {

    @Query("SELECT t FROM Translator t " +
            "LEFT JOIN t.languages l " +
            "LEFT JOIN t.serviceTypes st " +
            "LEFT JOIN t.specializations sp " +
            "LEFT JOIN t.location loc " +
            "WHERE (:availability IS NULL OR t.availability = :availability) " +
            "AND (:languages IS NULL OR l.id IN :languages) " +
            "AND (:serviceTypes IS NULL OR st.id IN :serviceTypes) " +
            "AND (:specializations IS NULL OR sp.id IN :specializations) " +
            "AND (:location IS NULL OR loc.id IN :location)")
    List<User> filterTranslators(
            @Param("availability") AvailabilityStatus availability,
            @Param("languages") List<Long> languages,
            @Param("serviceTypes") List<Long> serviceTypes,
            @Param("specializations") List<Long> specializations,
            @Param("location") List<Long> location
    );

    @Query("SELECT t FROM Translator t " +
            "WHERE LOWER(t.firstName) " +
            "LIKE LOWER(CONCAT('%', :username, '%')) " +
            "OR LOWER(t.lastName) LIKE LOWER(CONCAT('%', :username, '%'))")
    List<User> searchTranslatorsByUsername(String username);
}
