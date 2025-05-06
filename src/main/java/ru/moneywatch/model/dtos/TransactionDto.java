package ru.moneywatch.model.dtos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ru.moneywatch.model.enums.Category;
import ru.moneywatch.model.enums.StatusOperation;
import ru.moneywatch.model.enums.TypeTransaction;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Класс TransactionDto представляет собой объект передачи данных (DTO) для транзакций.
 */
@Builder
public record TransactionDto(
        @NotNull
        Date date,

        @NotBlank
        TypeTransaction typeTransaction,

        String comment,

        @Digits(integer = 10, fraction = 5)
        BigDecimal sum,

        StatusOperation status,

        long userAccountId,

        long bankAccountId,

        Category category) {
}
