package ru.moneywatch.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.moneywatch.model.dtos.TransactionDto;
import ru.moneywatch.model.entities.TransactionEntity;
import ru.moneywatch.model.enums.Category;
import ru.moneywatch.model.enums.StatusOperation;
import ru.moneywatch.model.enums.TypeTransaction;
import ru.moneywatch.model.mappers.TransactionMapper;
import ru.moneywatch.repository.AccountRepository;
import ru.moneywatch.repository.TransactionRepository;
import ru.moneywatch.service.TransactionService;

import java.util.Date;
import java.util.List;

/**
 * Реализация сервиса для работы с транзакциями.
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public List<TransactionDto> getAll() {
        return transactionRepository.findAll().stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    @Override
    public List<TransactionDto> getAllByFilter(StatusOperation status,
                                               Category category,
                                               TypeTransaction type,
                                               Long receiptAccountId,
                                               Long receiptCheckingAccountId,
                                               Date fromDate,
                                               Date toDate,
                                               Integer minSum,
                                               Integer maxSum,
                                               String inn) {
        return transactionRepository.filterTransactions(status, category, type, receiptAccountId, receiptCheckingAccountId,
                        fromDate, toDate, minSum, maxSum, inn).stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    @Override
    public TransactionDto getById(Long id) {
        var document = transactionRepository.findById(id)
                .orElseThrow();

        return transactionMapper.toDto(document);
    }

    @Override
    public TransactionDto create(TransactionDto document) {
        var documentEntity = transactionMapper.toEntity(document);

        return transactionMapper.toDto(
                transactionRepository.save(documentEntity)
        );
    }

    @Override
    public ResponseEntity<TransactionEntity> edit(Long id, TransactionDto transaction) {
        return transactionRepository.findById(id)
                .map(it -> updateTransactionFields(it, transaction))
                .map(transactionRepository::save)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private TransactionEntity updateTransactionFields(TransactionEntity transaction, TransactionDto req) {
        transaction.setDate(req.date());
        transaction.setStatus(req.status());
        transaction.setTypeTransaction(req.typeTransaction());
        transaction.setComment(req.comment());
        transaction.setSum(req.sum());
        transaction.setCategory(req.category());
        transaction.setBankAccount(accountRepository.findById(req.bankAccountId()).orElseThrow());
        transaction.setUserAccount(accountRepository.findById(req.userAccountId()).orElseThrow());

        return transaction;
    }

    @Override
    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }
}
