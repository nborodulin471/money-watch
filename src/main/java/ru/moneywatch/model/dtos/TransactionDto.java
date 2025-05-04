package ru.moneywatch.model.dtos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import ru.moneywatch.model.enums.Category;
import ru.moneywatch.model.enums.PersonType;
import ru.moneywatch.model.enums.StatusOperation;
import ru.moneywatch.model.enums.TypeTransaction;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Класс TransactionDto представляет собой объект передачи данных (DTO) для транзакций.
 */
@Builder
public record TransactionDto(

        @NotBlank
        PersonType personType,

        @NotNull
        Date date,

        @NotBlank
        TypeTransaction typeTransaction,

        String comment,

        @Digits(integer = 10, fraction = 5)
        BigDecimal sum,

        StatusOperation status,

        long senderBankId,

        long account,

        long recipientBankId,

        @Pattern(regexp = "\\d{11}")
        long recipientInn,

        long recipientAccountId,

        Category category,

        @Pattern(regexp = "^(8|\\+7)\\d{10}$")
        String receiptNumber) {
}
