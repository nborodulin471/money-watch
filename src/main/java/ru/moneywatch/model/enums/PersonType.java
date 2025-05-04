package ru.moneywatch.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Тип пользователя.
 */
@Getter
@RequiredArgsConstructor
public enum PersonType {

    INDIVIDUAL("INDIVIDUAL", "Физическое лицо"),

    LEGAL_ENTITY("LEGAL_ENTITY", "Юридическое лицо");

    private final String code;

    private final String name;
}
