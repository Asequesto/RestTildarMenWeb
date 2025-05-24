package kz.tildarmen.TildarMen.repository;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findAllByEmployerIdOrderByPublicationDateDesc(Long employerId);

    @Query("""
        SELECT j
        FROM Job j
        LEFT JOIN j.languages l
        LEFT JOIN j.serviceTypes st
        LEFT JOIN j.specializations sp
        LEFT JOIN j.location loc
        WHERE (:jobTitle IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :jobTitle, '%')))
          AND (:location IS NULL OR loc.id IN :location)
          AND (:startDate IS NULL OR j.startDate >= :startDate)
          AND (:endDate IS NULL OR j.endDate <= :endDate)
          AND (:languages IS NULL OR l.id IN :languages)
          AND (:serviceTypes IS NULL OR st.id IN :serviceTypes)
          AND (:specializations IS NULL OR sp.id IN :specializations)
        GROUP BY j
        HAVING
          (:languages IS NULL OR COUNT(DISTINCT l.id) = :languageSize)
          AND (:serviceTypes IS NULL OR COUNT(DISTINCT st.id) = :serviceTypeSize)
          AND (:specializations IS NULL OR COUNT(DISTINCT sp.id) = :specializationSize)
        ORDER BY j.publicationDate DESC
""")
    List<Job> filterJobs(
            @Param("languages") List<Long> languages,
            @Param("languageSize") int languageSize,
            @Param("serviceTypes") List<Long> serviceTypes,
            @Param("serviceTypeSize") int serviceTypeSize,
            @Param("specializations") List<Long> specializations,
            @Param("specializationSize") int specializationSize,
            @Param("location") List<Long> location,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("jobTitle") String jobTitle
    );


    List<Job> findTop8ByOrderByPublicationDateDesc();
}
