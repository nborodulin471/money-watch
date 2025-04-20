package ru.moneywatch.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import ru.moneywatch.model.StatusReport;

import java.util.Date;

/**
 * Сущность в БД для работы с отчетами.
 */
@Data
@Entity(name = "reports")
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String link;

    @OneToOne
    private UserEntity user;

    private Date dateCreated;

    private StatusReport status;

    private ReportType type;
}
