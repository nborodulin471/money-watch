package ru.moneywatch.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import ru.moneywatch.model.Category;
import ru.moneywatch.model.StatusOperation;
import ru.moneywatch.model.TypeTransaction;

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

    private TypeTransaction typeTransaction;

    private String comment;

    private int sum;

    private StatusOperation status;

    @ManyToOne
    private AccountEntity receiptAccount;

    @ManyToOne
    private BankEntity receiptBank;

    @ManyToOne
    private AccountEntity recipientCheckingAccount;

    private Category category;

}
