package ru.moneywatch.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.moneywatch.model.dtos.TransactionDto;
import ru.moneywatch.model.entities.TransactionEntity;
import ru.moneywatch.service.TransactionService;
import ru.moneywatch.service.auth.UserService;

import java.util.List;

/**
 * Контроллер для работы с документами.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transaction/")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllByUser(userService.getCurrentUser().getId()));
    }

    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(@RequestBody TransactionDto transaction) {
        return ResponseEntity.ok(transactionService.create(transaction));
    }

    @PostMapping("/{id}")
    public ResponseEntity<TransactionEntity> editTransaction(@PathVariable Long id, @RequestBody TransactionDto transaction) {
        if (userService.getCurrentUser().getId().equals(transaction.userId())) {
            return transactionService.edit(id, transaction);
        }
        return ResponseEntity.status(403).build();
    }

}
