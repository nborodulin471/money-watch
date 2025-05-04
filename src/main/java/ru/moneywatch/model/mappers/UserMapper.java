package ru.moneywatch.model.mappers;

import org.springframework.stereotype.Component;

import ru.moneywatch.model.dtos.UserDto;
import ru.moneywatch.model.entities.Role;
import ru.moneywatch.model.entities.User;

/**
 * Маппер для пользователей.
 */
@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(
                user.getUsername(),
                null,
                user.getRole().name(),
                user.getPersonType(),
                user.getInn()
        );
    }

    public User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setRole(Role.valueOf(userDto.getRole()));

        return user;
    }

}
