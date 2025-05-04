package ru.moneywatch.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Тип транзакции.
 */
@Getter
@RequiredArgsConstructor
public enum TypeTransaction {

    ADMISSION("ADMISSION", "Поступление"),

    WRITE_OFF("WRITE_OFF", "Списание");

    private final String code;

    private final String name;
}
