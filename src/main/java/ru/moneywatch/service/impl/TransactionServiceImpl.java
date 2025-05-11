package ru.moneywatch.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.moneywatch.model.dtos.TransactionDto;
import ru.moneywatch.model.entities.Role;
import ru.moneywatch.model.entities.TransactionEntity;
import ru.moneywatch.model.enums.StatusOperation;
import ru.moneywatch.model.mappers.TransactionMapper;
import ru.moneywatch.repository.AccountRepository;
import ru.moneywatch.repository.TransactionRepository;
import ru.moneywatch.service.TransactionService;
import ru.moneywatch.service.auth.UserService;
import ru.moneywatch.util.TransactionFilter;
import ru.moneywatch.util.TransactionSpecification;

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
    private final UserService userService;

    @Override
    public List<TransactionDto> getAll() {
        return transactionRepository.findAll().stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    @Override
    public List<TransactionDto> getAllByUser(long userId) {
        return transactionRepository.findAllById(userId).stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    @Override
    public List<TransactionDto> filterTransactions(TransactionFilter filter) {
        return transactionRepository.findAll(TransactionSpecification.withFilter(filter)).stream()
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
        documentEntity.setStatus(StatusOperation.NEW);

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
        var user = userService.getCurrentUser();
        if (user.getRole() == Role.ROLE_ADMIN) {
            transaction.setStatus(req.status());
        }

        transaction.setDate(req.date());
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
