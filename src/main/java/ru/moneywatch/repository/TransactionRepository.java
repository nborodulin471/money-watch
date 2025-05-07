package ru.moneywatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.moneywatch.model.enums.Category;
import ru.moneywatch.model.enums.StatusOperation;
import ru.moneywatch.model.enums.TypeTransaction;
import ru.moneywatch.model.entities.TransactionEntity;

import java.util.Date;
import java.util.List;

/**
 * Репозиторий для работы с транзакциями.
 */
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("""
        SELECT t FROM TransactionEntity t
        LEFT JOIN t.bankAccount ra
        LEFT JOIN ra.user u
        WHERE (:status IS NULL OR t.status = :status)
          AND (:category IS NULL OR t.category = :category)
          AND (:type IS NULL OR t.typeTransaction = :type)
          AND (:receiptAccountId IS NULL OR t.userAccount.id = :receiptAccountId)
          AND (:receiptCheckingAccountId IS NULL OR t.bankAccount.id = :receiptCheckingAccountId)
          AND (:fromDate IS NULL OR t.date >= :fromDate)
          AND (:toDate IS NULL OR t.date <= :toDate)
          AND (:minSum IS NULL OR t.sum >= :minSum)
          AND (:maxSum IS NULL OR t.sum <= :maxSum)
          AND (:inn IS NULL OR u.inn = :inn)
    """)
    List<TransactionEntity> filterTransactions(
            @Param("status") StatusOperation status,
            @Param("category") Category category,
            @Param("type") TypeTransaction type,
            @Param("userAccountId") Long userAccountId,
            @Param("bankAccountId") Long bankAccountId,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            @Param("minSum") Integer minSum,
            @Param("maxSum") Integer maxSum,
            @Param("inn") String inn
    );

    @Query(value = "SELECT EXTRACT(MONTH FROM date) AS month, type_transaction, COUNT(id) " +
            "FROM transactions " +
            "GROUP BY month, type_transaction " +
            "ORDER BY month, type_transaction",
            nativeQuery = true)
    List<Object[]> getMonthlyTransactionStatsRaw();

    @Query("SELECT t.typeTransaction, COUNT(t), SUM(t.sum) FROM TransactionEntity t " +
            "WHERE t.typeTransaction IN ('ADMISSION', 'WRITE_OFF') " +
            "GROUP BY t.typeTransaction")
    List<Object[]> getTransactionStatsByType();

    @Query("SELECT t.category, t.typeTransaction, SUM(t.sum) " +
            "FROM TransactionEntity t " +
            "WHERE t.category IS NOT NULL " +
            "GROUP BY t.category, t.typeTransaction")
    List<Object[]> getSumsByCategoryAndType();

    // Статистика по банкам-отправителям
    @Query("SELECT b.name, COUNT(t), SUM(t.sum) " +
            "FROM TransactionEntity t JOIN t.userAccount a JOIN a.bank b " +
            "GROUP BY b.name")
    List<Object[]> getStatsBySenderBank();

    // Статистика по банкам-получателям
    @Query("SELECT b.name, COUNT(t), SUM(t.sum) " +
            "FROM TransactionEntity t JOIN t.bankAccount a JOIN a.bank b " +
            "GROUP BY b.name")
    List<Object[]> getStatsByRecipientBank();

    @Query(value = """
    SELECT 
        CASE 
            WHEN :periodType = 'WEEK' THEN CONCAT('Week ', EXTRACT(WEEK FROM t.date), ', ', TO_CHAR(t.date, 'YYYY'))
            WHEN :periodType = 'MONTH' THEN CONCAT(TRIM(TO_CHAR(t.date, 'Month')), ' ', TO_CHAR(t.date, 'YYYY'))
            WHEN :periodType = 'QUARTER' THEN CONCAT('Q', EXTRACT(QUARTER FROM t.date), ' ', TO_CHAR(t.date, 'YYYY'))
            ELSE TO_CHAR(t.date, 'YYYY')
        END AS period,
        COUNT(t.id) AS transactionCount
    FROM transactions t
    WHERE t.date BETWEEN :startDate AND :endDate
    GROUP BY period
    ORDER BY MIN(t.date)
    """, nativeQuery = true)
    List<Object[]> getTransactionCountByPeriod(String periodType, Date startDate, Date endDate);
}
