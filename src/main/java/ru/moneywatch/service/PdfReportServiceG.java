package ru.moneywatch.service;

import org.jfree.chart.JFreeChart;
import ru.moneywatch.model.dtos.TransactionDynamicsDto;
import ru.moneywatch.model.dtos.TransactionStatsDto;

import java.io.IOException;
import java.util.List;

public class PdfReportServiceG {

    private static final ChartService chartService = new ChartService();
    private static final PDFService pdfService = new PDFService();

    // Метод для генерации отчета с графиками
    public static byte[] generateBankStatsReport(List<Object[]> senderStats, List<Object[]> recipientStats) throws IOException {
        JFreeChart senderChart = chartService.createChartFromData("Sending Banks", senderStats);
        JFreeChart recipientChart = chartService.createChartFromData("Recipient Banks", recipientStats);

        byte[] senderImage = chartService.createChartAsImage(senderChart);
        byte[] recipientImage = chartService.createChartAsImage(recipientChart);

        return pdfService.generatePDFWithTwoCharts(senderImage, recipientImage);
    }

    public static byte[] generateTransactionDynamicsReport(List<TransactionDynamicsDto> stats, String title) throws IOException {
        // Создаем график с использованием данных
        JFreeChart chart = chartService.createDynamicsChart(title, stats);

        // Генерируем изображение графика
        byte[] chartImage = chartService.createChartAsImage(chart);

        // Генерируем PDF с графиком
        return pdfService.generatePDFWithTitleAndChart(title, stats, chartImage);
    }

    public static byte[] generateTransactionStatsPdf(List<TransactionStatsDto> stats) throws IOException {
        String title = "Dynamics of transactions by types and months";

        // Строим график
        JFreeChart chart = chartService.createTransactionStatsBarChart(title, stats);
        byte[] chartImage = chartService.createChartAsImage(chart);

        // Генерируем PDF
        return pdfService.generatePDFWithTitleAndChartForStats(title, stats, chartImage);
    }
}
