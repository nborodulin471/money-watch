package ru.moneywatch.service;

import ru.moneywatch.model.dtos.TransactionStatsDto;

import java.io.IOException;
import java.util.List;

public interface PdfReportService {
    byte[] generateTransactionStatsPdf(List<TransactionStatsDto> stats) throws IOException;

    byte[] generateCompletedAndReturnTransactionsReport() throws IOException;

    byte[] generateTransactionSumReport() throws IOException;
}
