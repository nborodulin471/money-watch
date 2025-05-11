package ru.moneywatch.controller;

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
import ru.moneywatch.model.dtos.AccountDto;
import ru.moneywatch.model.dtos.TransactionDto;
import ru.moneywatch.model.dtos.UserDto;
import ru.moneywatch.model.entities.TransactionEntity;
import ru.moneywatch.model.mappers.AccountMapper;
import ru.moneywatch.service.AccountService;
import ru.moneywatch.service.TransactionService;
import ru.moneywatch.service.auth.UserService;
import ru.moneywatch.util.TransactionFilter;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/")
public class AdminController {

    private final TransactionService transactionService;
    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final UserService userService;

    @PostMapping("/transaction/{id}")
    public ResponseEntity<TransactionEntity> changeStatus(@PathVariable long id, @RequestBody TransactionDto transactionDto) {
        return transactionService.edit(id, transactionDto);
    }

    @DeleteMapping("/transaction")
    public ResponseEntity<?> deleteTransactionById(@RequestParam Long id) {
        transactionService.deleteById(id);
        return ResponseEntity.ok().build();
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

    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAll());
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(
                userService.findAll()
        );
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable long id) {
        return ResponseEntity.ok(
                userService.get(id)
        );
    }

    @PostMapping("/users/{id}")
    public ResponseEntity<UserDto> editUser(@PathVariable long id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(
                userService.edit(id, userDto)
        );
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.delete(id);
    }

    @PostMapping
    public ResponseEntity<AccountDto> create(@RequestBody AccountDto bankDto) {
        return
                ResponseEntity.ok(
                        accountMapper.toDto(accountService.create(bankDto))
                );
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        accountService.delete(id);
    }
}

