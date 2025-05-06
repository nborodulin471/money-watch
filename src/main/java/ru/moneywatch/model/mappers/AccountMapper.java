package ru.moneywatch.model.mappers;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.moneywatch.model.dtos.AccountDto;
import ru.moneywatch.model.entities.AccountEntity;
import ru.moneywatch.repository.BankRepository;
import ru.moneywatch.repository.UserRepository;

/**
 * Маппер для банков.
 */
@Component
@RequiredArgsConstructor
public class AccountMapper {

    private final UserRepository userRepository;
    private final BankRepository bankRepository;

    public AccountDto toDto(AccountEntity entity) {
        return new AccountDto(entity.getId(), entity.getAccount(), entity.getUser().getId(), entity.getBank().getId());
    }

    public AccountEntity toEntity(AccountDto dto) {
        return new AccountEntity(dto.id(),
                dto.account(),
                userRepository.findById(dto.userId()).orElseThrow(),
                bankRepository.findById(dto.bankId()).orElseThrow());
    }

}
