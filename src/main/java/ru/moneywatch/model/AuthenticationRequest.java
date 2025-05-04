package ru.moneywatch.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Запрос на аутентификацию.
 */
@Data
@AllArgsConstructor
public class AuthenticationRequest {
    private String username;
    private String password;
}