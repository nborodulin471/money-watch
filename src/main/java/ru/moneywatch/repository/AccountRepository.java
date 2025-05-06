package ru.moneywatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.moneywatch.model.entities.AccountEntity;

import java.util.List;

/**
 * Репозиторий для работы со счетами.
 */
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    List<AccountEntity> findAllByUserId(long userId);
}
