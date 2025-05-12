package ru.moneywatch.model.dtos;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    private String username;
    private String password;
    private String role;
    private PersonType personType;
    @Size(min = 8, max = 8, message = "ИНН должен быть 8 символов длиной")
    @Pattern(regexp = "^[0-9]*$", message = "ИНН должен содержать только цифры")
    private String inn;
}
