package ru.moneywatch.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.moneywatch.model.dtos.TransactionStatsDto;
import ru.moneywatch.model.entities.TransactionEntity;
import ru.moneywatch.model.enums.StatusOperation;
import ru.moneywatch.repository.TransactionRepository;
import ru.moneywatch.service.PdfReportService;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PdfReportServiceImpl implements PdfReportService {

    private final TransactionRepository transactionRepository;

    @Override
    public byte[] generateTransactionStatsPdf(List<TransactionStatsDto> stats) throws IOException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Заголовок отчета
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Dynamics of transactions by types and months", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            // Создание таблицы
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Шрифт для заголовков
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            headerFont.setColor(Color.WHITE);

            // Добавление заголовков
            addTableHeader(table, "Month", headerFont);
            addTableHeader(table, "Type transaction", headerFont);
            addTableHeader(table, "Amount", headerFont);

            // Шрифт для данных
            Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            contentFont.setColor(Color.BLACK);

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

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

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
}
