package ru.moneywatch.service;


import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.moneywatch.model.dtos.AccountDto;
import ru.moneywatch.model.entities.AccountEntity;
import ru.moneywatch.model.mappers.AccountMapper;
import ru.moneywatch.repository.AccountRepository;
import ru.moneywatch.service.auth.UserService;

import java.util.List;

/**
 * Сервис для работы с банками.
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final UserService userService;

    public List<AccountEntity> findAll() {
        var id = userService.getCurrentUser().getId();
        return accountRepository.findAllByUserId(id);
    }

    public AccountEntity create(AccountDto dto) {
        return accountRepository.save(
                accountMapper.toEntity(dto)
        );
    }

    public void delete(Long id) {
        accountRepository.deleteById(id);
    }
}
