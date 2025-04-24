package ru.moneywatch.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ru.moneywatch.model.Category;
import ru.moneywatch.model.StatusOperation;
import ru.moneywatch.model.TypeTransaction;
import ru.moneywatch.model.dtos.TransactionDto;
import ru.moneywatch.model.entities.TransactionEntity;
import ru.moneywatch.service.TransactionService;

import java.util.Date;
import java.util.List;

/**
 * Контроллер для работы с документами.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/transaction/")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public List<TransactionDto> getAllTransactions() {
        return transactionService.getAll();
    }

    @GetMapping("/filter")
    public List<TransactionDto> getAllFilterTransactions(
            @RequestParam(required = false) StatusOperation status,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) TypeTransaction type,
            @RequestParam(required = false) Long receiptAccountId,
            @RequestParam(required = false) Long receiptCheckingAccountId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
            @RequestParam(required = false) Integer minSum,
            @RequestParam(required = false) Integer maxSum,
            @RequestParam(required = false) String inn
    ) {
        return transactionService.getAllByFilter(status, category, type, receiptAccountId, receiptCheckingAccountId,
                fromDate, toDate, minSum, maxSum, inn);
    }

    @GetMapping("/{id}")
    public TransactionDto getTransactionById(@PathVariable Long id) {
        return transactionService.getById(id);
    }

    @PostMapping
    public TransactionDto createTransaction(@RequestBody TransactionDto transaction) {
        return transactionService.create(transaction);
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
