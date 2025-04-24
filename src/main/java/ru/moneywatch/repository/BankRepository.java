package ru.moneywatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.moneywatch.model.entities.BankEntity;

/**
 * Репозиторий для работы со счетами.
 */
public interface BankRepository extends JpaRepository<BankEntity, Long> {
}
