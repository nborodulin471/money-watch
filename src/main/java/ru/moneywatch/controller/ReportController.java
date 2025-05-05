package ru.moneywatch.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.moneywatch.model.dtos.TransactionStatsDto;
import ru.moneywatch.model.enums.TypeTransaction;
import ru.moneywatch.repository.TransactionRepository;
import ru.moneywatch.service.PdfReportService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final TransactionRepository transactionRepository;
    private final PdfReportService pdfReportService;

    public ReportController(TransactionRepository transactionRepository, PdfReportService pdfReportService) {
        this.transactionRepository = transactionRepository;
        this.pdfReportService = pdfReportService;
    }

    @GetMapping(value = "/transaction-stats", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getTransactionStatsPdf() {
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

    @GetMapping("/transaction-amount")
    public ResponseEntity<byte[]> getTransactionsReport() throws IOException {
        byte[] pdf = pdfReportService.generateCompletedAndReturnTransactionsReport();

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
}
