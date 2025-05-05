package ru.moneywatch.model.dtos;

import lombok.Getter;
import lombok.Setter;
import ru.moneywatch.model.enums.TypeTransaction;

@Setter
@Getter
public class TransactionStatsDto {

    private Integer month;
    private TypeTransaction typeTransaction;
    private Long count;

    public TransactionStatsDto(Integer month, TypeTransaction typeTransaction, Long count) {
        this.month = month;
        this.typeTransaction = typeTransaction;
        this.count = count;
    }
}
