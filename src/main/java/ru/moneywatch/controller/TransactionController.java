package ru.moneywatch.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.moneywatch.model.dtos.TransactionDto;
import ru.moneywatch.model.entities.TransactionEntity;
import ru.moneywatch.service.TransactionService;
import ru.moneywatch.util.TransactionFilter;

import java.util.List;

/**
 * Контроллер для работы с документами.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transaction/")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAll());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TransactionDto>> getFilteredTransactions(
            TransactionFilter filter
    ) {
        return ResponseEntity.ok(transactionService.filterTransactions(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getById(id));
    }

    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(@RequestBody TransactionDto transaction) {
        return ResponseEntity.ok(transactionService.create(transaction));
    }

    @PostMapping("/{id}")
    public ResponseEntity<TransactionEntity> editTransaction(@PathVariable Long id, @RequestBody TransactionDto transaction) {
        return transactionService.edit(id, transaction);
    }

    @DeleteMapping
    public void deleteTransactionById(@RequestParam Long id) {
        transactionService.deleteById(id);
    }

}
