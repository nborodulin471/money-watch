package ru.moneywatch.service;

import lombok.RequiredArgsConstructor;
import org.jfree.chart.JFreeChart;
import org.springframework.stereotype.Service;
import ru.moneywatch.model.TransactionStats;
import ru.moneywatch.model.dtos.TransactionDynamicsDto;
import ru.moneywatch.model.dtos.TransactionStatsDto;
import ru.moneywatch.model.enums.Category;
import ru.moneywatch.model.enums.StatusOperation;
import ru.moneywatch.model.enums.TypeTransaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class PdfReportServiceG {

    private final ChartService chartService;
    private final PDFService pdfService;
    private final TransactionService transactionService;

    // Метод для генерации отчета с графиками
    public byte[] generateBankStatsReport(List<Object[]> senderStats, List<Object[]> recipientStats) {
        JFreeChart senderChart = chartService.createChartFromData("Sending Banks", senderStats);
        JFreeChart recipientChart = chartService.createChartFromData("Recipient Banks", recipientStats);

        byte[] senderImage = chartService.createChartAsImage(senderChart);
        byte[] recipientImage = chartService.createChartAsImage(recipientChart);

        return pdfService.generatePDFWithTwoCharts(senderImage, recipientImage);
    }

    public byte[] generateTransactionDynamicsReport(List<TransactionDynamicsDto> stats, String title) {
        // Создаем график с использованием данных
        JFreeChart chart = chartService.createDynamicsChart(title, stats);

        // Генерируем изображение графика
        byte[] chartImage = chartService.createChartAsImage(chart);

        // Генерируем PDF с графиком
        return pdfService.generatePDFWithTitleAndChart(title, stats, chartImage);
    }

    public byte[] generateTransactionStatsPdf(List<TransactionStatsDto> stats) {
        String title = "Dynamics of transactions by types and months";

        // Строим график
        JFreeChart chart = chartService.createTransactionStatsBarChart(title, stats);
        byte[] chartImage = chartService.createChartAsImage(chart);

        // Генерируем PDF
        return pdfService.generatePDFWithTitleAndChartForStats(title,
                BigDecimal.valueOf(stats.stream().mapToLong(TransactionStatsDto::getCount).sum()), chartImage);
    }

    public byte[] generateCategorySummaryPdf() {
        String title = "Report by categories";

        Map<Category, Map<TypeTransaction, BigDecimal>> categoryStats = transactionService.getCategoryStats();

        // Вычисляем итоговую сумму
        BigDecimal totalAdmission = BigDecimal.ZERO;
        BigDecimal totalWriteOff = BigDecimal.ZERO;

        for (Category category : categoryStats.keySet()) {
            Map<TypeTransaction, BigDecimal> typeSums = categoryStats.get(category);

            totalAdmission = totalAdmission.add(typeSums.getOrDefault(TypeTransaction.ADMISSION, BigDecimal.ZERO));
            totalWriteOff = totalWriteOff.add(typeSums.getOrDefault(TypeTransaction.WRITE_OFF, BigDecimal.ZERO));
        }

        BigDecimal total = totalAdmission.subtract(totalWriteOff);

        // Строим график
        JFreeChart chart = chartService.createCategoryStatsBarChart(title, categoryStats);
        byte[] chartImage = chartService.createChartAsImage(chart);

        // Генерируем PDF
        return pdfService.generatePDFWithTitleAndChartForStats(title, total, chartImage);
    }

    public byte[] generateTransactionSumPdf() {
        String title = "SUM OF TRANSACTIONS REPORT";

        // Получаем статистику из БД
        Map<TypeTransaction, TransactionStats> stats = transactionService.getTransactionStatsByType();

        // Вычисляем итоговую сумму
        BigDecimal admissionSum = stats.getOrDefault(TypeTransaction.ADMISSION,
                new TransactionStats(0L, BigDecimal.ZERO)).getSum();
        BigDecimal writeOffSum = stats.getOrDefault(TypeTransaction.WRITE_OFF,
                new TransactionStats(0L, BigDecimal.ZERO)).getSum();
        BigDecimal difference = admissionSum.subtract(writeOffSum);

        // Строим график
        JFreeChart chart = chartService.createTransactionSumBarChart(title, stats);
        byte[] chartImage = chartService.createChartAsImage(chart);

        // Генерируем PDF
        return pdfService.generatePDFWithTitleAndChartForStats(title, difference, chartImage);
    }

    public byte[] generateCompletedAndReturnedTransactionsPdf() {
        String title = "Transaction report\nAmount of transactions by status";

        // Получаем данные из репозитория
        Map<StatusOperation, Long> stats = transactionService.getCompletedAndReturnedTransactionsStats();

        // Вычисляем итоговую сумму
        Long total = stats.values().stream().mapToLong(Long::longValue).sum();

        // Строим график
        JFreeChart chart = chartService.createCompletedAndReturnedTransactionsBarChart(title, stats);
        byte[] chartImage = chartService.createChartAsImage(chart);

        // Генерируем PDF
        return pdfService.generatePDFWithTitleAndChartForStats(title, total, chartImage);
    }
}
