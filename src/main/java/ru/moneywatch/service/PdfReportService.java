package ru.moneywatch.service;

import ru.moneywatch.model.dtos.TransactionStatsDto;
import ru.moneywatch.model.enums.PeriodType;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface PdfReportService {
    byte[] generateTransactionStatsPdf(List<TransactionStatsDto> stats) throws IOException;

    byte[] generateCompletedAndReturnTransactionsReport() throws IOException;

    byte[] generateTransactionSumReport() throws IOException;

    byte[] generateCategorySummaryReport() throws IOException;

    byte[] generateBankStatsReport() throws IOException;

    byte[] generateTransactionDynamicsReport(PeriodType periodType, Date startDate, Date endDate) throws IOException;
}
