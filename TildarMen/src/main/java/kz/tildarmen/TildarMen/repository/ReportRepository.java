package kz.tildarmen.TildarMen.repository;

import kz.tildarmen.TildarMen.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
