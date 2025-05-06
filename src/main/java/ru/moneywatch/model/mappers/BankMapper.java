package ru.moneywatch.model.mappers;

import org.springframework.stereotype.Component;

import ru.moneywatch.model.dtos.BankDto;
import ru.moneywatch.model.entities.BankEntity;

/**
 * Маппер для банков.
 */
@Component
public class BankMapper {

    public BankDto toDto(BankEntity entity) {
        return new BankDto(entity.getId(), entity.getName(), entity.getBic());
    }

    public BankEntity toEntity(BankDto bankDto) {
        return new BankEntity(bankDto.id(), bankDto.name(), bankDto.bic());
    }

}
