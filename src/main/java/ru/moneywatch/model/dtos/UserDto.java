package ru.moneywatch.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.moneywatch.model.enums.PersonType;

/**
 * Пользователь.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class UserDto {
    private Long id;
    private String username;
    private String password;
    private boolean enabled;
    private String role;
    private PersonType personType;
    private String inn;
}
