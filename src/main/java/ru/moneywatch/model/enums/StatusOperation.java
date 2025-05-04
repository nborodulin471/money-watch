package ru.moneywatch.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Статус операции.
 */
@Getter
@RequiredArgsConstructor
public enum StatusOperation {

    NEW("NEW", "Новая"),

    CONFIRMED("CONFIRMED", "Подтвержденная"),

    IN_PROCESS("IN_PROCESS", "В обработке"),

    CANCELED("CANCELED", "Отменена"),

    COMPLETED("COMPLETED", "Платеж выполнен"),

    DELETED("DELETED", "Платеж удален"),

    RETURN("RETURN", "Возврат");

    private final String code;

    private final String name;
}
