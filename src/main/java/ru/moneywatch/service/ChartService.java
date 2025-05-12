package ru.moneywatch.service;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import ru.moneywatch.model.dtos.TransactionDynamicsDto;
import ru.moneywatch.model.dtos.TransactionStatsDto;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import javax.imageio.ImageIO;

public class ChartService {

    public JFreeChart createChartFromData(String title, List<Object[]> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Object[] row : data) {
            String bankName = (String) row[0];
            Number amount = (Number) row[2];
            dataset.addValue(amount.doubleValue(), "Transactions", bankName);
        }

        return ChartFactory.createBarChart(
                title,
                "Bank",
                "Transaction Sum",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );
    }

    // Метод для конвертации графика в изображение
    public byte[] createChartAsImage(JFreeChart chart) throws IOException {
        // Генерация изображения из графика
        BufferedImage image = chart.createBufferedImage(600, 400);

        // Конвертация изображения в байты (например, в формат PNG)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        return baos.toByteArray();
    }

    public JFreeChart createDynamicsChart(String title, List<TransactionDynamicsDto> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Обрабатываем данные по месяцам
        for (TransactionDynamicsDto dto : data) {
            String period = dto.getPeriod(); // Это будет месяц (например, "2025-01")
            Long transactionCount = dto.getTransactionCount(); // Количество банков

            // Добавляем данные в dataset
            dataset.addValue(transactionCount, "Banks", period); // Используем период как категорию на оси X
        }

        // Создаем линейный график
        return ChartFactory.createLineChart(
                title,             // Заголовок графика
                "Month",           // Ось X (Месяцы)
                "Number of Banks", // Ось Y (Количество банков)
                dataset,           // Данные для графика
                PlotOrientation.VERTICAL,
                true,              // Легенда
                true,              // Подсказки
                false              // Ссылки
        );
    }

    public JFreeChart createTransactionStatsBarChart(String title, List<TransactionStatsDto> stats) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (TransactionStatsDto dto : stats) {
            String monthLabel = Month.of(dto.getMonth())
                    .getDisplayName(TextStyle.SHORT, Locale.ENGLISH); // Jan, Feb и т.д.
            dataset.addValue(dto.getCount(), dto.getTypeTransaction().toString(), monthLabel);
        }

        return ChartFactory.createBarChart(
                title,
                "Month",                   // X-axis label
                "Transaction Count",       // Y-axis label
                dataset,
                PlotOrientation.VERTICAL,
                true,                      // include legend
                true,
                false
        );
    }


}
