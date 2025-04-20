package ru.moneywatch.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.moneywatch.model.dtos.ReportDto;
import ru.moneywatch.model.mappers.ReportMapper;
import ru.moneywatch.repository.ReportRepository;
import ru.moneywatch.service.ReportService;

import java.util.List;

/**
 * Сервис для работы с отчетами.
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    @Override
    public List<ReportDto> getReports() {
        return reportRepository.findAll().stream()
                .map(reportMapper::toDto)
                .toList();
    }
}
