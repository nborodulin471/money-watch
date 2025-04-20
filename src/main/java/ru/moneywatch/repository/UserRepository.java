package ru.moneywatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.moneywatch.model.entities.UserEntity;

/**
 * Репозиторий, который отвечает за работу с пользователями.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}
