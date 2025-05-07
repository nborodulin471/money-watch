package ru.moneywatch.util;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.moneywatch.model.entities.TransactionEntity;

import java.util.ArrayList;
import java.util.List;


public class TransactionSpecification {
    public static Specification<TransactionEntity> withFilter(TransactionFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getUserAccountId() != null) {
                predicates.add(cb.equal(root.get("userAccount").get("id"), filter.getUserAccountId()));
            }

            if (filter.getBankAccountId() != null) {
                predicates.add(cb.equal(root.get("bankAccount").get("id"), filter.getBankAccountId()));
            }

            if (filter.getDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), filter.getDateFrom()));
            }

            if (filter.getDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), filter.getDateTo()));
            }

            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }

            if (filter.getInn() != null) {
                predicates.add(cb.equal(root.get("userAccount").get("user").get("inn"), filter.getInn()));
            }

            if (filter.getSumFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("sum"), filter.getSumFrom()));
            }

            if (filter.getSumTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("sum"), filter.getSumTo()));
            }

            if (filter.getTypeTransaction() != null) {
                predicates.add(cb.equal(root.get("typeTransaction"), filter.getTypeTransaction()));
            }

            if (filter.getCategory() != null) {
                predicates.add(cb.equal(root.get("category"), filter.getCategory()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
