package ru.moneywatch.service;

import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Service;
import ru.moneywatch.model.TransactionStats;
import ru.moneywatch.model.dtos.TransactionDynamicsDto;
import ru.moneywatch.model.dtos.TransactionStatsDto;
import ru.moneywatch.model.enums.Category;
import ru.moneywatch.model.enums.StatusOperation;
import ru.moneywatch.model.enums.TypeTransaction;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.imageio.ImageIO;

@Service
@Slf4j
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
    public byte[] createChartAsImage(JFreeChart chart) {
        // Генерация изображения из графика
        BufferedImage image = chart.createBufferedImage(600, 400);

        // Конвертация изображения в байты (например, в формат PNG)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "PNG", baos);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
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

    public JFreeChart createCategoryStatsBarChart(String title, Map<Category, Map<TypeTransaction, BigDecimal>> stats) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Category category : stats.keySet()) {
            Map<TypeTransaction, BigDecimal> typeSums = stats.get(category);

            BigDecimal admission = typeSums.getOrDefault(TypeTransaction.ADMISSION, BigDecimal.ZERO);
            BigDecimal writeOff = typeSums.getOrDefault(TypeTransaction.WRITE_OFF, BigDecimal.ZERO);
            BigDecimal total = admission.subtract(writeOff);

            dataset.addValue(total, total.compareTo(BigDecimal.ZERO) >= 0
                    ? TypeTransaction.ADMISSION : TypeTransaction.WRITE_OFF, category);
        }

        return ChartFactory.createBarChart(
                title,
                "Category",                   // X-axis label
                "Transaction Type",       // Y-axis label
                dataset,
                PlotOrientation.VERTICAL,
                true,                      // include legend
                true,
                false
        );
    }

    public JFreeChart createTransactionSumBarChart(String title, Map<TypeTransaction, TransactionStats> stats) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(stats.getOrDefault(TypeTransaction.ADMISSION, new TransactionStats(0L, BigDecimal.ZERO)).getSum(),
                TypeTransaction.ADMISSION,
                stats.getOrDefault(TypeTransaction.ADMISSION, new TransactionStats(0L, BigDecimal.ZERO)).getCount());
        dataset.addValue(stats.getOrDefault(TypeTransaction.WRITE_OFF, new TransactionStats(0L, BigDecimal.ZERO)).getSum(),
                TypeTransaction.WRITE_OFF,
                stats.getOrDefault(TypeTransaction.WRITE_OFF, new TransactionStats(0L, BigDecimal.ZERO)).getCount());

        return ChartFactory.createBarChart(
                title,
                "Amount of transactions",                   // X-axis label
                "Transaction Type",       // Y-axis label
                dataset,
                PlotOrientation.VERTICAL,
                true,                      // include legend
                true,
                false
        );
    }

    public JFreeChart createCompletedAndReturnedTransactionsBarChart(String title, Map<StatusOperation, Long> stats) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(stats.getOrDefault(StatusOperation.COMPLETED, 0L),
                StatusOperation.COMPLETED,
                stats.getOrDefault(StatusOperation.COMPLETED, 0L));
        dataset.addValue(stats.getOrDefault(StatusOperation.RETURN, 0L),
                StatusOperation.RETURN,
                stats.getOrDefault(StatusOperation.RETURN, 0L));

        return ChartFactory.createBarChart(
                title,
                "Sum of transactions",                   // X-axis label
                "Operation Status",       // Y-axis label
                dataset,
                PlotOrientation.VERTICAL,
                true,                      // include legend
                true,
                false
        );
    }
}
