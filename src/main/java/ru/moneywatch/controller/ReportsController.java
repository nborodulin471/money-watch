package ru.moneywatch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import ru.moneywatch.model.dtos.ReportDto;
import ru.moneywatch.service.ReportService;

import java.util.List;

/**
 * Контроллер для работы с отчетами.
 */
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReportsController {

    private final ReportService reportService;

    @GetMapping("/reports")
    public List<ReportDto> getReports() {
        return reportService.getReports();
    }
}