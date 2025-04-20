package ru.moneywatch.model.dtos;

import java.util.Date;

/**
 * Класс ReportDto представляет собой объект передачи данных (DTO) для отчетов.
 */
public record ReportDto(
        Long id,
        String link,
        String username,
        Date dateCreated,
        String status,
        String type) {
}
