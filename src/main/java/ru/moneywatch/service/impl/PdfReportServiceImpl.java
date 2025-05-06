package ru.moneywatch.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.moneywatch.model.dtos.TransactionStatsDto;
import ru.moneywatch.model.entities.TransactionEntity;
import ru.moneywatch.model.enums.Category;
import ru.moneywatch.model.enums.StatusOperation;
import ru.moneywatch.model.enums.TypeTransaction;
import ru.moneywatch.repository.TransactionRepository;
import ru.moneywatch.service.PdfReportService;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PdfReportServiceImpl implements PdfReportService {

    private final TransactionRepository transactionRepository;

    private final Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
    private final Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
    private final Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

    @Override
    public byte[] generateTransactionStatsPdf(List<TransactionStatsDto> stats) throws IOException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Заголовок отчета
            Paragraph title = new Paragraph("Dynamics of transactions by types and months", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            // Создание таблицы
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Добавление заголовков
            addTableHeader(table, "Month", headerFont);
            addTableHeader(table, "Type transaction", headerFont);
            addTableHeader(table, "Amount", headerFont);

            // Заполнение данными
            for (TransactionStatsDto dto : stats) {
                addTableCell(table, dto.getMonth().toString(), contentFont);
                addTableCell(table, dto.getTypeTransaction().toString(), contentFont);
                addTableCell(table, String.valueOf(dto.getCount()), contentFont);
            }

            document.add(table);

        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }

        return out.toByteArray();
    }

    @Override
    public byte[] generateCompletedAndReturnTransactionsReport() throws IOException {
        // Получаем данные из репозитория
        List<TransactionEntity> transactions = transactionRepository.findAll();

        // Фильтруем и группируем транзакции по статусам
        Map<StatusOperation, Long> stats = transactions.stream()
                .filter(t -> t.getStatus() == StatusOperation.COMPLETED || t.getStatus() == StatusOperation.RETURN)
                .collect(Collectors.groupingBy(
                        TransactionEntity::getStatus,
                        Collectors.counting()
                ));

        // Создаем документ PDF
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Заголовок отчета
            Paragraph title = new Paragraph("Transaction report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            // Подзаголовок
            Paragraph subtitle = new Paragraph("Amount of transactions by status", headerFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(15f);
            document.add(subtitle);

            // Создаем таблицу
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(60);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.setSpacingBefore(10f);

            // Заголовки таблицы
            addTableCell(table, "Transaction status", headerFont);
            addTableCell(table, "Amount", headerFont);

            // Данные таблицы
            addTableCell(table, "COMPLETED", contentFont);
            addTableCell(table, String.valueOf(stats.getOrDefault(StatusOperation.COMPLETED, 0L)), contentFont);

            addTableCell(table, "RETURNED", contentFont);
            addTableCell(table, String.valueOf(stats.getOrDefault(StatusOperation.RETURN, 0L)), contentFont);

            document.add(table);

            // Итоговая информация
            long total = stats.values().stream().mapToLong(Long::longValue).sum();
            Paragraph totalParagraph = new Paragraph(
                    "Total transactions: " + total,
                    headerFont
            );
            totalParagraph.setAlignment(Element.ALIGN_CENTER);
            totalParagraph.setSpacingBefore(20f);
            document.add(totalParagraph);

        } finally {
            document.close();
        }

        return out.toByteArray();
    }

    @Override
    public byte[] generateTransactionSumReport() throws IOException {
        // Получаем статистику из БД
        Map<TypeTransaction, TransactionStats> stats = transactionRepository.getTransactionStatsByType()
                .stream()
                .collect(Collectors.toMap(
                        arr -> (TypeTransaction) arr[0],
                        arr -> new TransactionStats((Long) arr[1], (BigDecimal) arr[2])
                ));

        // Создаем PDF документ
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Заголовок отчета
            Paragraph title = new Paragraph("SUM OF TRANSACTIONS REPORT", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            // Создаем таблицу (3 колонки)
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(80);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.setSpacingBefore(10f);

            // Заголовки таблицы
            addTableCell(table, "Transaction type", headerFont);
            addTableCell(table, "AMOUNT", headerFont);
            addTableCell(table, "SUM", headerFont);

            // Данные таблицы
            BigDecimal admissionSum = stats.getOrDefault(TypeTransaction.ADMISSION,
                    new TransactionStats(0L, BigDecimal.ZERO)).getSum();
            BigDecimal writeOffSum = stats.getOrDefault(TypeTransaction.WRITE_OFF,
                    new TransactionStats(0L, BigDecimal.ZERO)).getSum();
            BigDecimal difference = admissionSum.subtract(writeOffSum);

            addTableRow(table, "ADMISSION",
                    stats.getOrDefault(TypeTransaction.ADMISSION,
                            new TransactionStats(0L, BigDecimal.ZERO)),
                    contentFont);

            addTableRow(table, "WRITE_OFF",
                    stats.getOrDefault(TypeTransaction.WRITE_OFF,
                            new TransactionStats(0L, BigDecimal.ZERO)),
                    contentFont);

            // Итоговая строка
            PdfPCell cell = new PdfPCell(new Phrase("RESULT:", contentFont));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(difference.toString(), contentFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            document.add(table);

            // Анализ результатов
            Paragraph analysis = new Paragraph();
            analysis.setSpacingBefore(20f);

            if (difference.compareTo(BigDecimal.ZERO) > 0) {
                analysis.add(new Chunk("Excess of revenue over expenditure: ", contentFont));
                analysis.add(new Chunk(difference.toString(), contentFont));
            } else if (difference.compareTo(BigDecimal.ZERO) < 0) {
                analysis.add(new Chunk("Excess of expenses over receipts: ", contentFont));
                analysis.add(new Chunk(difference.abs().toString(), contentFont));
            } else {
                analysis.add(new Chunk("Income and expenses are equal", contentFont));
            }

            analysis.setAlignment(Element.ALIGN_CENTER);
            document.add(analysis);

        } finally {
            document.close();
        }

        return out.toByteArray();
    }

    @Override
    public byte[] generateCategorySummaryReport() throws IOException {
        // Получаем данные из БД
        List<Object[]> results = transactionRepository.getSumsByCategoryAndType();

        // Группируем данные по категориям
        Map<Category, Map<TypeTransaction, BigDecimal>> categoryStats = results.stream()
                .collect(Collectors.groupingBy(
                        arr -> (Category) arr[0],
                        Collectors.toMap(
                                arr -> (TypeTransaction) arr[1],
                                arr -> (BigDecimal) arr[2]
                        )
                ));

        // Создаем PDF документ
        Document document = new Document(PageSize.A4.rotate()); // Горизонтальная ориентация
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            // Заголовок отчета
            Paragraph title = new Paragraph("Report by categories", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            // Создаем таблицу (Категория, Поступления, Расходы, Итого)
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);

            // Заголовки таблицы
            addTableHeader(table, "Category", headerFont);
            addTableHeader(table, "ADMISSIONS", headerFont);
            addTableHeader(table, "WRITE_OFF", headerFont);
            addTableHeader(table, "RESULT", headerFont);

            // Заполняем таблицу данными
            BigDecimal totalAdmission = BigDecimal.ZERO;
            BigDecimal totalWriteOff = BigDecimal.ZERO;

            for (Category category : categoryStats.keySet()) {
                Map<TypeTransaction, BigDecimal> typeSums = categoryStats.get(category);

                BigDecimal admission = typeSums.getOrDefault(TypeTransaction.ADMISSION, BigDecimal.ZERO);
                BigDecimal writeOff = typeSums.getOrDefault(TypeTransaction.WRITE_OFF, BigDecimal.ZERO);
                BigDecimal total = admission.subtract(writeOff);

                addCategoryRow(table, category.name(),
                        admission, writeOff, total,
                        contentFont, headerFont);

                totalAdmission = totalAdmission.add(admission);
                totalWriteOff = totalWriteOff.add(writeOff);
            }

            // Итоговая строка
            addTotalRow(table, "TOTAL",
                    totalAdmission, totalWriteOff,
                    totalAdmission.subtract(totalWriteOff),
                    headerFont);

            document.add(table);

            // Добавляем анализ
            addAnalysis(document, totalAdmission, totalWriteOff);

        } finally {
            document.close();
        }

        return out.toByteArray();
    }

    @Override
    public byte[] generateBankStatsReport() throws IOException {
        // Получаем данные из БД
        List<Object[]> senderStats = transactionRepository.getStatsBySenderBank();
        List<Object[]> recipientStats = transactionRepository.getStatsByRecipientBank();

        // Создаем PDF документ
        Document document = new Document(PageSize.A4.rotate()); // Горизонтальная ориентация
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            // Заголовок отчета
            Paragraph title = new Paragraph("Statistics on banks", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            // Раздел по банкам-отправителям
            Paragraph senderTitle = new Paragraph("Sending banks", contentFont);
            senderTitle.setSpacingAfter(10f);
            document.add(senderTitle);

            PdfPTable senderTable = createBankStatsTable(senderStats, headerFont, contentFont);
            document.add(senderTable);

            // Раздел по банкам-получателям
            Paragraph recipientTitle = new Paragraph("Recipient banks", contentFont);
            recipientTitle.setSpacingBefore(20f);
            recipientTitle.setSpacingAfter(10f);
            document.add(recipientTitle);

            PdfPTable recipientTable = createBankStatsTable(recipientStats, headerFont, contentFont);
            document.add(recipientTable);

            // Сравнительный анализ
            addComparativeAnalysis(document, senderStats, recipientStats, contentFont);

        } finally {
            document.close();
        }

        return out.toByteArray();
    }

    private PdfPTable createBankStatsTable(List<Object[]> stats, Font headerFont, Font contentFont) {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        // Заголовки таблицы
        addTableHeader(table, "Bank", headerFont);
        addTableHeader(table, "Amount of transactions", headerFont);
        addTableHeader(table, "Sum of transactions", headerFont);

        // Заполняем таблицу данными
        for (Object[] row : stats) {
            String bankName = (String) row[0];
            Long count = (Long) row[1];
            BigDecimal sum = (BigDecimal) row[2];

            table.addCell(createCell(bankName, contentFont));
            table.addCell(createCell(count.toString(), contentFont));
            table.addCell(createCell(formatMoney(sum), contentFont));
        }

        // Итоговая строка
        addTotalRow(table, stats, headerFont);

        return table;
    }

    private void addComparativeAnalysis(Document document,
                                        List<Object[]> senderStats,
                                        List<Object[]> recipientStats,
                                        Font font) throws DocumentException {

        Paragraph analysis = new Paragraph();
        analysis.setSpacingBefore(20f);

        // Находим топ-банки
        String topSender = senderStats.stream()
                .max(Comparator.comparing(row -> (BigDecimal) row[2]))
                .map(row -> (String) row[0])
                .orElse("no data");

        String topRecipient = recipientStats.stream()
                .max(Comparator.comparing(row -> (BigDecimal) row[2]))
                .map(row -> (String) row[0])
                .orElse("no data");

        analysis.add(new Chunk("Analysis: ", font));
        analysis.add(new Chunk("• The largest volume of outgoing payments: ", font));
        analysis.add(new Chunk(topSender + "\n", font));
        analysis.add(new Chunk("• The largest volume of incoming payments: ", font));
        analysis.add(new Chunk(topRecipient + "\n", font));

        analysis.setAlignment(Element.ALIGN_LEFT);
        document.add(analysis);
    }

    private void addTableHeader(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(Color.GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5f);
        table.addCell(cell);
    }

    private void addTableCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5f);
        table.addCell(cell);
    }

    private String formatMoney(BigDecimal amount) {
        return String.format("%,.2f", amount) + " ₽";
    }

    private void addTableRow(PdfPTable table, String type, TransactionStats stats, Font font) {
        addTableCell(table, type, font);
        addTableCell(table, stats.getCount().toString(), font);
        addTableCell(table, stats.getSum().toString(), font);
    }

    private void addAnalysis(Document document, BigDecimal totalAdmission, BigDecimal totalWriteOff) throws DocumentException {

        Paragraph analysis = new Paragraph();
        analysis.setSpacingBefore(20f);

        analysis.add(new Chunk("Total admissions: ", headerFont));
        analysis.add(new Chunk(formatMoney(totalAdmission) + "\n", contentFont));

        analysis.add(new Chunk("Total write-off: ", headerFont));
        analysis.add(new Chunk(formatMoney(totalWriteOff) + "\n", contentFont));

        BigDecimal balance = totalAdmission.subtract(totalWriteOff);
        analysis.add(new Chunk("Result balance: ", headerFont));
        analysis.add(new Chunk(formatMoney(balance), contentFont));

        analysis.setAlignment(Element.ALIGN_CENTER);
        document.add(analysis);
    }

    private PdfPCell createCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5f);
        return cell;
    }

    private void addCategoryRow(PdfPTable table, String category,
                                BigDecimal admission, BigDecimal writeOff,
                                BigDecimal total, Font contentFont, Font highlightFont) {
        table.addCell(createCell(category, contentFont));
        table.addCell(createCell(formatMoney(admission), contentFont));
        table.addCell(createCell(formatMoney(writeOff), contentFont));
        table.addCell(createCell(formatMoney(total), highlightFont));
    }

    private void addTotalRow(PdfPTable table, String label,
                             BigDecimal totalAdmission, BigDecimal totalWriteOff,
                             BigDecimal balance, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(label, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setColspan(1);
        table.addCell(cell);

        table.addCell(createCell(formatMoney(totalAdmission), font));
        table.addCell(createCell(formatMoney(totalWriteOff), font));

        cell = new PdfPCell(new Phrase(formatMoney(balance), font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addTotalRow(PdfPTable table, List<Object[]> stats, Font font) {
        BigDecimal totalSum = stats.stream()
                .map(row -> (BigDecimal) row[2])
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        PdfPCell cell = new PdfPCell(new Phrase("Result:", font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setColspan(2);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(formatMoney(totalSum), font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    @Data
    @AllArgsConstructor
    private static class TransactionStats {
        private Long count;
        private BigDecimal sum;
    }
}
