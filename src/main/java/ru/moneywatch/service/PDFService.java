package ru.moneywatch.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Image;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import ru.moneywatch.model.dtos.TransactionDynamicsDto;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PDFService {


    public byte[] generatePDFWithTwoCharts(byte[] chart1, byte[] chart2) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Bank Statistics Report"));

        document.add(new Paragraph("Sending Banks Chart"));
        Image img1 = new Image(com.itextpdf.io.image.ImageDataFactory.create(chart1));
        document.add(img1);

        document.add(new Paragraph("Recipient Banks Chart"));
        Image img2 = new Image(com.itextpdf.io.image.ImageDataFactory.create(chart2));
        document.add(img2);

        document.close();
        return baos.toByteArray();
    }

    public byte[] generatePDFWithTitleAndChart(String title, List<TransactionDynamicsDto> stats, byte[] chartImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Добавляем заголовок
        document.add(new Paragraph(title));

        // Добавляем сам график
        Image image = new Image(com.itextpdf.io.image.ImageDataFactory.create(chartImage));
        document.add(image);

        // Подсчитываем общее количество банков за весь период
        long totalBanks = stats.stream().mapToLong(TransactionDynamicsDto::getTransactionCount).sum();

        // Добавляем строку с общим количеством банков
        document.add(new Paragraph("Total Number of Banks: " + totalBanks));

        // Закрываем документ
        document.close();
        return baos.toByteArray();
    }
    public byte[] generatePDFWithTitleAndChartForStats(String title, Number total, byte[] chartImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Заголовок
        document.add(new Paragraph(title));

        // Вставка графика
        Image image = new Image(com.itextpdf.io.image.ImageDataFactory.create(chartImage));
        image.setAutoScale(true);
        document.add(image);

        // Итоговая сумма
        document.add(new Paragraph("Total: " + total));

        document.close();
        return baos.toByteArray();
    }

}
