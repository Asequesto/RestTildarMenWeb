package kz.tildarmen.TildarMen.services;

import kz.tildarmen.TildarMen.dto.TransactionDto;
import kz.tildarmen.TildarMen.mapper.TransactionMapper;
import kz.tildarmen.TildarMen.model.Employer;
import kz.tildarmen.TildarMen.model.Transaction;
import kz.tildarmen.TildarMen.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final EmployerService employerService;

    public List<TransactionDto> getAllTransaction(Long id){
        Employer employer = employerService.getEmployerById(id);
        List<Transaction> transactions = transactionRepository.findAllByEmployer(employer);
        return transactionMapper.toDtoList(transactions);
    }

}
