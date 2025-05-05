package ru.moneywatch.model.entities;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Данные счета.
 */
@Data
@Entity
@Table(name = "accounts")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String account;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private BankEntity bank;

}
