package ru.moneywatch.model.mappers;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.moneywatch.model.dtos.TransactionDto;
import ru.moneywatch.model.entities.TransactionEntity;
import ru.moneywatch.repository.AccountRepository;
import ru.moneywatch.repository.UserRepository;

/**
 * Маппер для транзакций.
 */
@Component
@RequiredArgsConstructor
public class TransactionMapper {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public TransactionDto toDto(TransactionEntity entity) {
        if (entity == null) {
            return null;
        }

        return TransactionDto.builder()
                .date(entity.getDate())
                .typeTransaction(entity.getTypeTransaction())
                .comment(entity.getComment())
                .sum(entity.getSum())
                .status(entity.getStatus())
                .userAccountId(entity.getUserAccount().getId())
                .bankAccountId(entity.getBankAccount().getId())
                .userId(entity.getId())
                .category(entity.getCategory())
                .build();
    }

    public TransactionEntity toEntity(TransactionDto dto) {
        if (dto == null) {
            return null;
        }

        TransactionEntity entity = new TransactionEntity();
        entity.setDate(dto.date());
        entity.setTypeTransaction(dto.typeTransaction());
        entity.setComment(dto.comment());
        entity.setSum(dto.sum());
        entity.setStatus(dto.status());
        entity.setUserAccount(accountRepository.findById(dto.userAccountId()).orElseThrow());
        entity.setBankAccount(accountRepository.findById(dto.bankAccountId()).orElseThrow());
        entity.setUser(userRepository.findById(dto.userId()).orElseThrow());
        entity.setCategory(dto.category());

        return entity;
    }
}
