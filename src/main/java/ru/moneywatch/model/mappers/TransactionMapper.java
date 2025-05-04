package ru.moneywatch.model.mappers;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.moneywatch.model.dtos.TransactionDto;
import ru.moneywatch.model.entities.TransactionEntity;
import ru.moneywatch.repository.AccountRepository;
import ru.moneywatch.repository.BankRepository;

/**
 * Маппер для транзакций.
 */
@Component
@RequiredArgsConstructor
public class TransactionMapper {

    private final AccountRepository accountRepository;
    private final BankRepository bankRepository;

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
                .recipientAccountId(entity.getReceiptAccount().getId())
                .recipientBankId(entity.getReceiptBank().getId())
                .category(entity.getCategory())
                .senderBankId(entity.getReceiptBank().getId())
                .account(entity.getReceiptAccount().getId())
                .recipientInn(Long.parseLong(entity.getReceiptAccount().getUser().getInn()))
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
        entity.setReceiptAccount(accountRepository.findById(dto.recipientAccountId()).orElseThrow());
        entity.setReceiptBank(bankRepository.findById(dto.recipientBankId()).orElseThrow());
        entity.setCategory(dto.category());

        return entity;
    }
}
