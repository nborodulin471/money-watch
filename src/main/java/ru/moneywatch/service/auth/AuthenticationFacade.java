package ru.moneywatch.service.auth;

import org.springframework.security.core.Authentication;

/**
 * Фасад для получения текущего контекста аутентификации.
 */
public interface AuthenticationFacade {

    Authentication getAuthentication();

}
