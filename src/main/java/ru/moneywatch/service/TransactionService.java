package ru.moneywatch.service;

import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import ru.moneywatch.model.Category;
import ru.moneywatch.model.StatusOperation;
import ru.moneywatch.model.TypeTransaction;
import ru.moneywatch.model.dtos.TransactionDto;
import ru.moneywatch.model.entities.TransactionEntity;

import java.util.Date;
import java.util.List;

/**
 * Сервис для работы с транзакциями.
 */
public interface TransactionService {

    /**
     * Возвращает все транзакции.
     */
    List<TransactionDto> getAll();

    /**
     * Возвращает все транзакции по параметрам поиска.
     */
    List<TransactionDto> getAllByFilter(StatusOperation status,
                                        Category category,
                                        TypeTransaction type,
                                        Long receiptAccountId,
                                        Long receiptCheckingAccountId,
                                        Date fromDate,
                                        Date toDate,
                                        Integer minSum,
                                        Integer maxSum,
                                        String inn);

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
