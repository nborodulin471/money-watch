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
        LEFT JOIN t.receiptAccount ra
        LEFT JOIN ra.user u
        WHERE (:status IS NULL OR t.status = :status)
          AND (:category IS NULL OR t.category = :category)
          AND (:type IS NULL OR t.typeTransaction = :type)
          AND (:receiptAccountId IS NULL OR t.receiptAccount.id = :receiptAccountId)
          AND (:receiptCheckingAccountId IS NULL OR t.recipientCheckingAccount.id = :receiptCheckingAccountId)
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
            @Param("receiptAccountId") Long receiptAccountId,
            @Param("receiptCheckingAccountId") Long receiptCheckingAccountId,
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
}
