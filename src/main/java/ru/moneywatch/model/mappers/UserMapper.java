package ru.moneywatch.model.mappers;

import org.springframework.stereotype.Component;

import ru.moneywatch.model.dtos.UserDto;
import ru.moneywatch.model.entities.UserEntity;

/**
 * Маппер для пользователей.
 */
@Component
public class UserMapper {

    public UserDto toDto(UserEntity user) {
        if (user == null) {
            return null;
        }
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.getRole()
        );
    }

    public UserEntity toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        UserEntity user = new UserEntity();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEnabled(userDto.isEnabled());
        user.setRole(userDto.getRole());

        return user;
    }

}
