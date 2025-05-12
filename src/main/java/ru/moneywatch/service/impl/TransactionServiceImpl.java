package ru.moneywatch.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.moneywatch.model.TransactionStats;
import ru.moneywatch.model.dtos.TransactionDto;
import ru.moneywatch.model.entities.Role;
import ru.moneywatch.model.entities.TransactionEntity;
import ru.moneywatch.model.enums.Category;
import ru.moneywatch.model.enums.StatusOperation;
import ru.moneywatch.model.enums.TypeTransaction;
import ru.moneywatch.model.mappers.TransactionMapper;
import ru.moneywatch.repository.AccountRepository;
import ru.moneywatch.repository.TransactionRepository;
import ru.moneywatch.service.TransactionService;
import ru.moneywatch.service.auth.UserService;
import ru.moneywatch.util.TransactionFilter;
import ru.moneywatch.util.TransactionSpecification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        var user = userService.getCurrentUser();
        if (user.getRole() == Role.ROLE_ADMIN) {
            documentEntity.setUser(userService.getCurrentUser());
        }

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

    public Map<Category, Map<TypeTransaction, BigDecimal>> getCategoryStats() {
        // Получаем данные из БД
        List<Object[]> stats = transactionRepository.getSumsByCategoryAndType();

        // Группируем данные по категориям
        return stats.stream()
                .collect(Collectors.groupingBy(
                        arr -> (Category) arr[0],
                        Collectors.toMap(
                                arr -> (TypeTransaction) arr[1],
                                arr -> (BigDecimal) arr[2]
                        )
                ));
    }

    public Map<TypeTransaction, TransactionStats> getTransactionStatsByType() {
        return transactionRepository.getTransactionStatsByType()
                .stream()
                .collect(Collectors.toMap(
                        arr -> (TypeTransaction) arr[0],
                        arr -> new TransactionStats((Long) arr[1], (BigDecimal) arr[2])
                ));
    }

    public Map<StatusOperation, Long> getCompletedAndReturnedTransactionsStats() {
        // Получаем данные из репозитория
        List<TransactionEntity> transactions = transactionRepository.findAll();

        // Фильтруем и группируем транзакции по статусам
        return transactions.stream()
                .filter(t -> t.getStatus() == StatusOperation.COMPLETED || t.getStatus() == StatusOperation.RETURN)
                .collect(Collectors.groupingBy(
                        TransactionEntity::getStatus,
                        Collectors.counting()
                ));
    }
}
