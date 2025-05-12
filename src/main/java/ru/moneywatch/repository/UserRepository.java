package ru.moneywatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.moneywatch.model.entities.User;

import java.util.Optional;

/**
 * Репозиторий, который отвечает за работу с пользователями.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByInn(String inn);
}
