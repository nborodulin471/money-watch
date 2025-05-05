package ru.moneywatch.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.moneywatch.model.dtos.TransactionStatsDto;
import ru.moneywatch.service.PdfReportService;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfReportServiceImpl implements PdfReportService {

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
