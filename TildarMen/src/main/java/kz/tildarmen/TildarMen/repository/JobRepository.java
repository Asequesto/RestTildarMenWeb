package kz.tildarmen.TildarMen.repository;

import jakarta.transaction.Transactional;
import kz.tildarmen.TildarMen.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findAllByEmployerId(Long employerId);

    @Query("SELECT j FROM Job j " +
            "LEFT JOIN j.languages l " +
            "LEFT JOIN j.serviceTypes st " +
            "LEFT JOIN j.specializations sp " +
            "LEFT JOIN j.locations loc " +
            "WHERE (:languages IS NULL OR l.id IN :languages) " +
            "AND (:serviceTypes IS NULL OR st.id IN :serviceTypes) " +
            "AND (:specializations IS NULL OR sp.id IN :specializations) " +
            "AND (:location IS NULL OR loc.id IN :location)")
    List<Job> filterJobs(
            @Param("languages") List<Long> languages,
            @Param("serviceTypes") List<Long> serviceTypes,
            @Param("specializations") List<Long> specializations,
            @Param("location") List<Long> location
    );

    @Query("SELECT j FROM Job j " +
            "WHERE LOWER(j.title) " +
            "LIKE LOWER(CONCAT('%', :jobTitle, '%'))")
    List<Job> findAllByTitle(String jobTitle);
}
