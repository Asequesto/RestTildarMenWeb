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

    @Query("""
        SELECT DISTINCT t
        FROM Translator t
        LEFT JOIN t.languages l
        LEFT JOIN t.serviceTypes st
        LEFT JOIN t.specializations sp
        LEFT JOIN t.location loc
        WHERE (:username IS NULL OR LOWER(t.firstName) LIKE LOWER(CONCAT('%', :username, '%'))
                                OR LOWER(t.lastName) LIKE LOWER(CONCAT('%', :username, '%')))
          AND (:availability IS NULL OR t.availability = :availability)
          AND (:location IS NULL OR loc.id IN :location)
          AND (:languages IS NULL OR l.id IN :languages)
          AND (:serviceTypes IS NULL OR st.id IN :serviceTypes)
          AND (:specializations IS NULL OR sp.id IN :specializations)
        GROUP BY t
        HAVING
          (:languages IS NULL OR COUNT(DISTINCT l.id) = :languageSize)
          AND (:serviceTypes IS NULL OR COUNT(DISTINCT st.id) = :serviceTypeSize)
          AND (:specializations IS NULL OR COUNT(DISTINCT sp.id) = :specializationSize)
        """)
    List<Translator> filterTranslators(
            @Param("username") String username,
            @Param("availability") AvailabilityStatus availability,
            @Param("languages") List<Long> languages,
            @Param("languageSize") int languageSize,
            @Param("serviceTypes") List<Long> serviceTypes,
            @Param("serviceTypeSize") int serviceTypeSize,
            @Param("specializations") List<Long> specializations,
            @Param("specializationSize") int specializationSize,
            @Param("location") List<Long> location
    );



}
