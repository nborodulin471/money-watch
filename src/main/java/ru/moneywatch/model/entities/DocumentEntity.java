package ru.moneywatch.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import ru.moneywatch.model.Category;
import ru.moneywatch.model.PersonType;
import ru.moneywatch.model.StatusOperation;
import ru.moneywatch.model.TypeTransaction;

import java.util.Date;

/**
 * Сущность в БД для работы с документами.
 */
@Data
@Entity(name = "documents")
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private PersonType personType;

    private Date date;

    private TypeTransaction typeTransaction;

    private String comment;

    private int sum;

    private StatusOperation status;

    private String receiptAccount;

    private String receiptBank;

    private String inn;

    private String recipientCheckingAccount;

    private Category category;

    private String receiptNumber;

}
