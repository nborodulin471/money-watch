package ru.moneywatch.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.moneywatch.model.dtos.UserDto;
import ru.moneywatch.model.mappers.UserMapper;
import ru.moneywatch.repository.UserRepository;

/**
 * Сервис для работы с пользователями
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto get(long id) {
        return userMapper.toDto(
                userRepository.findById(id).orElseThrow()
        );
    }

    public UserDto edit(long id, UserDto userDto) {
        var user = userRepository.findById(id).orElseThrow();

        user.setEnabled(userDto.isEnabled());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        user.setInn(userDto.getInn());
        user.setPersonType(userDto.getPersonType());

        return userMapper.toDto(userRepository.save(user));
    }

    public void delete(long id) {
        var user = userRepository.findById(id).orElseThrow();
        user.setEnabled(false);
        userRepository.save(user);
    }
}
