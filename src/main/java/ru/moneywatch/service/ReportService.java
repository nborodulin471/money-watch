package ru.moneywatch.service;

import ru.moneywatch.model.dtos.ReportDto;

import java.util.List;

/**
 * Сервис для работы с отчетами.
 */
public interface ReportService {

    /**
     * Получает отчеты.
     */
    List<ReportDto> getReports();

}
