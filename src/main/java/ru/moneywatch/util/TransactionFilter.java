package ru.moneywatch.util;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.moneywatch.model.enums.Category;
import ru.moneywatch.model.enums.StatusOperation;
import ru.moneywatch.model.enums.TypeTransaction;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionFilter {
    private Long userAccountId;
    private Long bankAccountId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date dateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date dateTo;

    private StatusOperation status;
    private String inn;
    private BigDecimal sumFrom;
    private BigDecimal sumTo;
    private TypeTransaction typeTransaction;
    private Category category;
}
