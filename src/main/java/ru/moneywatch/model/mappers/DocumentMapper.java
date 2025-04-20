package ru.moneywatch.model.mappers;

import org.springframework.stereotype.Component;

import ru.moneywatch.model.dtos.DocumentDto;
import ru.moneywatch.model.entities.DocumentEntity;

/**
 * Маппер для документов.
 */
@Component
public class DocumentMapper {

    public DocumentDto toDto(DocumentEntity entity) {
        if (entity == null) {
            return null;
        }

        return DocumentDto.builder()
                .id(entity.getId())
                .personType(entity.getPersonType())
                .date(entity.getDate())
                .typeTransaction(entity.getTypeTransaction())
                .comment(entity.getComment())
                .sum(entity.getSum())
                .status(entity.getStatus())
                .receiptAccount(entity.getReceiptAccount())
                .receiptBank(entity.getReceiptBank())
                .inn(entity.getInn())
                .recipientCheckingAccount(entity.getRecipientCheckingAccount())
                .category(entity.getCategory())
                .receiptNumber(entity.getReceiptNumber())
                .build();
    }

    public DocumentEntity toEntity(DocumentDto dto) {
        if (dto == null) {
            return null;
        }

        DocumentEntity entity = new DocumentEntity();
        entity.setId(dto.id());
        entity.setPersonType(dto.personType());
        entity.setDate(dto.date());
        entity.setTypeTransaction(dto.typeTransaction());
        entity.setComment(dto.comment());
        entity.setSum(dto.sum());
        entity.setStatus(dto.status());
        entity.setReceiptAccount(dto.receiptAccount());
        entity.setReceiptBank(dto.receiptBank());
        entity.setInn(dto.inn());
        entity.setRecipientCheckingAccount(dto.recipientCheckingAccount());
        entity.setCategory(dto.category());
        entity.setReceiptNumber(dto.receiptNumber());

        return entity;
    }
}
