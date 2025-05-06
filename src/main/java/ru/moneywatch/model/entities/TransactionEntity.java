package ru.moneywatch.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import ru.moneywatch.model.enums.Category;
import ru.moneywatch.model.enums.StatusOperation;
import ru.moneywatch.model.enums.TypeTransaction;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Сущность в БД для работы с транзакциями.
 */
@Data
@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;

    @Enumerated(EnumType.STRING)
    private TypeTransaction typeTransaction;

    private String comment;

    private BigDecimal sum;

    @Enumerated(EnumType.STRING)
    private StatusOperation status;

    @ManyToOne(fetch = FetchType.LAZY)
    private AccountEntity userAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    private AccountEntity bankAccount;

    @Enumerated(EnumType.STRING)
    private Category category;

}
