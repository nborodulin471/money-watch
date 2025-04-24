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
                .id(entity.getId())
                .date(entity.getDate())
                .typeTransaction(entity.getTypeTransaction())
                .comment(entity.getComment())
                .sum(entity.getSum())
                .status(entity.getStatus())
                .receiptAccountId(entity.getReceiptAccount().getId())
                .receiptBankId(entity.getReceiptBank().getId())
                .recipientCheckingAccountId(entity.getRecipientCheckingAccount().getId())
                .category(entity.getCategory())
                .build();
    }

    public TransactionEntity toEntity(TransactionDto dto) {
        if (dto == null) {
            return null;
        }

        TransactionEntity entity = new TransactionEntity();
        entity.setId(dto.id());
        entity.setDate(dto.date());
        entity.setTypeTransaction(dto.typeTransaction());
        entity.setComment(dto.comment());
        entity.setSum(dto.sum());
        entity.setStatus(dto.status());
        entity.setReceiptAccount(accountRepository.findById(dto.receiptAccountId()).orElseThrow());
        entity.setReceiptBank(bankRepository.findById(dto.receiptBankId()).orElseThrow());
        entity.setRecipientCheckingAccount(accountRepository.findById(dto.recipientCheckingAccountId()).orElseThrow());
        entity.setCategory(dto.category());

        return entity;
    }
}
