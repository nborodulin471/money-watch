package ru.moneywatch.service;

import ru.moneywatch.model.dtos.TransactionDto;

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
     * Получает транзакцию по ее ид.
     */
    TransactionDto getById(Long id);

    /**
     * Создает транзакцию.
     */
    TransactionDto create(TransactionDto document);

    /**
     * Редактирует транзакцию.
     */
    TransactionDto edit(Long id, TransactionDto document);

    /**
     * Удаляет транзакцию по ее идентификатору.
     */
    void deleteById(Long id);

}
