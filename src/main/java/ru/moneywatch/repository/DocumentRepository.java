package ru.moneywatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.moneywatch.model.entities.DocumentEntity;

/**
 * Репозиторий для работы с документами.
 */
public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
}
