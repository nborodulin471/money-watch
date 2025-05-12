package ru.moneywatch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.moneywatch.model.dtos.TransactionStatsDto;
import ru.moneywatch.model.dtos.TransactionDynamicsDto;
import ru.moneywatch.model.enums.PeriodType;
import ru.moneywatch.model.enums.TypeTransaction;
import ru.moneywatch.repository.TransactionRepository;
import ru.moneywatch.service.PdfReportService;
import ru.moneywatch.service.PdfReportServiceG;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final TransactionRepository transactionRepository;
    private final PdfReportService pdfReportService;
    private final PdfReportServiceG pdfReportServiceG;

    @GetMapping(value = "/transaction-stats", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getTransactionStatsPdf2() {
        try {
            List<TransactionStatsDto> stats = getMonthlyStats();
            byte[] pdfBytes = pdfReportService.generateTransactionStatsPdf(stats);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=transaction-stats.pdf");

            return ResponseEntity.ok()
                    .headers(headers).
                    contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/transaction-stats-chart", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getTransactionStatsPdf() {
        try {
            List<TransactionStatsDto> stats = getMonthlyStats();
            byte[] pdfBytes = pdfReportServiceG.generateTransactionStatsPdf(stats);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=transaction-stats.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/transaction-amount")
    public ResponseEntity<byte[]> getTransactionsReport() throws IOException {
        byte[] pdf = pdfReportService.generateCompletedAndReturnTransactionsReport();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/transaction-amount-chart")
    public ResponseEntity<byte[]> getTransactionsReportWithChart() {
        byte[] pdf = pdfReportServiceG.generateCompletedAndReturnedTransactionsPdf();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/transaction-sum")
    public ResponseEntity<byte[]> getTransactionTypesReport() throws IOException {
        byte[] pdf = pdfReportService.generateTransactionSumReport();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transaction_types_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/transaction-sum-chart")
    public ResponseEntity<byte[]> getTransactionTypesReportWithChart() {
        byte[] pdf = pdfReportServiceG.generateTransactionSumPdf();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transaction_types_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/category-summary")
    public ResponseEntity<byte[]> getCategorySummaryReport() throws IOException {
        byte[] pdf = pdfReportService.generateCategorySummaryReport();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=category_summary.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/category-summary-chart")
    public ResponseEntity<byte[]> getCategorySummaryReportWithChart() {
        byte[] pdfWithChart = pdfReportServiceG.generateCategorySummaryPdf();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=category_summary.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfWithChart);
    }

    public List<TransactionStatsDto> getMonthlyStats() {
        List<Object[]> rawStats = transactionRepository.getMonthlyTransactionStatsRaw();

        List<TransactionStatsDto> result = new ArrayList<>();
        for (Object[] row : rawStats) {
            Integer month = ((Number) row[0]).intValue();
            String typeStr = (String) row[1];
            TypeTransaction type = TypeTransaction.valueOf(typeStr);
            Long count = ((Number) row[2]).longValue();

            result.add(new TransactionStatsDto(month, type, count));
        }

        return result;
    }

    @GetMapping("/bank-stats")
    public ResponseEntity<byte[]> getBankStatsReport2() throws IOException {
        byte[] pdf = pdfReportService.generateBankStatsReport();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bank_statistics.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/bank-stats-chart")
    public ResponseEntity<byte[]> getBankStatsReport() {
        List<Object[]> senderStats = transactionRepository.getStatsBySenderBank();
        List<Object[]> recipientStats = transactionRepository.getStatsByRecipientBank();

        byte[] pdf = pdfReportServiceG.generateBankStatsReport(senderStats, recipientStats);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bank_statistics.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/transaction-dynamic")
    public ResponseEntity<byte[]> getTransactionDynamicsReport2(
            @RequestParam PeriodType periodType,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws IOException {

        byte[] pdf = pdfReportService.generateTransactionDynamicsReport(periodType, startDate, endDate);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transaction_dynamics.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/transaction-dynamics-chart")
    public ResponseEntity<byte[]> getTransactionDynamicsReport(
            @RequestParam PeriodType periodType,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        // Получаем данные из репозитория (например, по месяцам)
        List<Object[]> rawData = transactionRepository.getTransactionCountByPeriod(
                periodType.toString(), startDate, endDate
        );

        // Преобразуем данные в DTO
        List<TransactionDynamicsDto> stats = rawData.stream()
                .map(arr -> new TransactionDynamicsDto((String) arr[0], (Long) arr[1]))
                .collect(Collectors.toList());

        // Формируем заголовок отчета
        String title = String.format("Transaction Dynamics (%s) from %tF to %tF", periodType, startDate, endDate);

        // Генерируем PDF с графиком
        byte[] pdf = pdfReportServiceG.generateTransactionDynamicsReport(stats, title);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transaction_dynamics.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
