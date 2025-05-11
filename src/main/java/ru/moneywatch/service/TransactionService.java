package ru.moneywatch.service;

import org.springframework.http.ResponseEntity;
import ru.moneywatch.model.dtos.TransactionDto;
import ru.moneywatch.model.entities.TransactionEntity;
import ru.moneywatch.util.TransactionFilter;

import java.util.List;

/**
 * Сервис для работы с транзакциями.
 */
public interface TransactionService {

    /**
     * Возвращает все транзакции.
     */
    List<TransactionDto> getAllByUser(long userId);

    /**
     * Возвращает все транзакции.
     */
    List<TransactionDto> getAll();

    /**
     * Возвращает все транзакции по параметрам поиска.
     */
    List<TransactionDto> filterTransactions(TransactionFilter filter);

    /**
     * Получает транзакцию по ее ид.
     */
    TransactionDto getById(Long id);

    /**
     * Создает транзакцию.
     */
    TransactionDto create(TransactionDto transaction);

    /**
     * Редактирует транзакцию.
     */
    ResponseEntity<TransactionEntity> edit(Long id, TransactionDto transaction);

    /**
     * Удаляет транзакцию по ее идентификатору.
     */
    void deleteById(Long id);

}
