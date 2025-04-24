package ru.moneywatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.moneywatch.model.entities.TransactionEntity;

/**
 * Репозиторий для работы с транзакциями.
 */
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
