package ru.moneywatch.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransactionStats {
    private Long count;
    private BigDecimal sum;
}
