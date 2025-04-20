package ru.moneywatch.model.dtos;

import lombok.Builder;
import ru.moneywatch.model.Category;
import ru.moneywatch.model.PersonType;
import ru.moneywatch.model.StatusOperation;
import ru.moneywatch.model.TypeTransaction;

import java.util.Date;

/**
 * Класс DocumentDto представляет собой объект передачи данных (DTO) для документов.
 */
@Builder
public record DocumentDto(
        Long id,
        PersonType personType,
        Date date,
        TypeTransaction typeTransaction,
        String comment,
        int sum,
        StatusOperation status,
        String receiptAccount,
        String receiptBank,
        String inn,
        String recipientCheckingAccount,
        Category category,
        String receiptNumber) {
}
