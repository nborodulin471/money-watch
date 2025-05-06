package ru.moneywatch.service;


import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.moneywatch.model.dtos.BankDto;
import ru.moneywatch.model.entities.BankEntity;
import ru.moneywatch.model.mappers.BankMapper;
import ru.moneywatch.repository.BankRepository;

import java.util.List;

/**
 * Сервис для работы с банками.
 */
@Service
@RequiredArgsConstructor
public class BankService {

    private final BankRepository bankRepository;
    private final BankMapper bankMapper;

    public BankEntity createBank(BankDto bankDto) {
        return bankRepository.save(
                bankMapper.toEntity(bankDto)
        );
    }

    public void delete(Long id) {
        bankRepository.deleteById(id);
    }

    public List<BankEntity> findAll() {
        return bankRepository.findAll();
    }
}
