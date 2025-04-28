package kz.tildarmen.TildarMen.repository;

import kz.tildarmen.TildarMen.model.Employer;
import kz.tildarmen.TildarMen.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByEmployer(Employer employer);
}
