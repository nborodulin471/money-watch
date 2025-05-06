package ru.moneywatch.model.dtos;

/**
 * Данные счета.
 */
public record AccountDto(Long id, String account, long userId, long bankId) {

}
