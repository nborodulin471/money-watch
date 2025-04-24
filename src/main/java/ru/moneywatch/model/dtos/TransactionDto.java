package ru.moneywatch.model.dtos;

import lombok.Builder;
import ru.moneywatch.model.Category;
import ru.moneywatch.model.StatusOperation;
import ru.moneywatch.model.TypeTransaction;

import java.util.Date;

/**
 * Класс TransactionDto представляет собой объект передачи данных (DTO) для транзакций.
 */
@Builder
public record TransactionDto(
        long id,
        Date date,
        TypeTransaction typeTransaction,
        String comment,
        int sum,
        StatusOperation status,
        long receiptAccountId,
        long receiptBankId,
        long recipientCheckingAccountId,
        Category category,
        String receiptNumber) {
}
