package ru.moneywatch.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.moneywatch.model.dtos.TransactionDto;
import ru.moneywatch.model.mappers.TransactionMapper;
import ru.moneywatch.repository.TransactionRepository;
import ru.moneywatch.service.TransactionService;

import java.util.List;

/**
 * Реализация сервиса для работы с транзакциями.
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public List<TransactionDto> getAll() {
        return transactionRepository.findAll().stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    @Override
    public TransactionDto getById(Long id) {
        var document = transactionRepository.findById(id)
                .orElseThrow();

        return transactionMapper.toDto(document);
    }

    @Override
    public TransactionDto create(TransactionDto document) {
        var documentEntity = transactionMapper.toEntity(document);

        return transactionMapper.toDto(
                transactionRepository.save(documentEntity)
        );
    }

    @Override
    public TransactionDto edit(Long id, TransactionDto document) {
        var actualDocument = transactionRepository.findById(id)
                .orElseThrow();

        // TODO тут должна быть логика по мержу двух объектов

        return transactionMapper.toDto(
                transactionRepository.save(actualDocument)
        );
    }

    @Override
    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }
}
