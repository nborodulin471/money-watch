package ru.moneywatch.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionDynamicsDto {
    private String period;
    private Long transactionCount;
}
