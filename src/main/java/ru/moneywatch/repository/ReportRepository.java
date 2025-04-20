package ru.moneywatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.moneywatch.model.entities.ReportEntity;

/**
 * Репозиторий для работы с отчетами.
 */
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {
}
