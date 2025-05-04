package ru.moneywatch.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import ru.moneywatch.model.dtos.UserDto;
import ru.moneywatch.model.entities.UserEntity;
import ru.moneywatch.model.mappers.UserMapper;
import ru.moneywatch.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    private UserMapper userMapper = new UserMapper();

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void testGetExistingUser() {
        // given
        long id = 1L;
        var expectedUser = new UserEntity();
        when(userRepository.findById(id)).thenReturn(Optional.of(expectedUser));

        // when
        var result = userService.get(id);

        // then
        assertThat(result).isEqualToComparingFieldByField(userMapper.toDto(expectedUser));
    }

    @Test
    void testEditUser() {
        // given
        long id = 1L;
        var existingUser = new UserEntity(); // инициализация существующего пользователя
        var updatedUserDto = new UserDto(); // данные для обновления
        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(UserEntity.class))).thenAnswer(i -> i.getArguments()[0]); // возвращаем аргумент

        ArgumentCaptor<UserEntity> argumentCaptor = ArgumentCaptor.forClass(UserEntity.class);

        // when
        UserDto result = userService.edit(id, updatedUserDto);

        // then
        verify(userRepository).save(argumentCaptor.capture());
        assertThat(result).isEqualToComparingFieldByField(userMapper.toDto(argumentCaptor.getValue()));
    }

    @Test
    void testDeleteUser() {
        // given
        long id = 1L;
        var existingUser = new UserEntity();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any())).thenReturn(new UserEntity());

        // when
        userService.delete(id);

        // then
        verify(userRepository).findById(id);
        verify(userRepository).save(same(existingUser));
        assertFalse(existingUser.isEnabled());
    }
}