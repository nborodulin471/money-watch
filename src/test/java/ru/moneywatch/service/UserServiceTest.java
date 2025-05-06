package ru.moneywatch.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import ru.moneywatch.model.dtos.UserDto;
import ru.moneywatch.model.entities.Role;
import ru.moneywatch.model.entities.User;
import ru.moneywatch.model.mappers.UserMapper;
import ru.moneywatch.repository.UserRepository;
import ru.moneywatch.service.auth.UserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
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
        var expectedUser = new User();
        expectedUser.setRole(Role.ROLE_USER);
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
        var existingUser = new User();
        existingUser.setRole(Role.ROLE_USER);
        var updatedUserDto = new UserDto();
        updatedUserDto.setRole(Role.ROLE_ADMIN.name());
        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]); // возвращаем аргумент

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);

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

        // when
        userService.delete(id);

        // then
        verify(userRepository).deleteById(id);
    }
}