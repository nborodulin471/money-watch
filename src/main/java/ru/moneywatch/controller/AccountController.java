package ru.moneywatch.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.moneywatch.model.dtos.AccountDto;
import ru.moneywatch.model.mappers.AccountMapper;
import ru.moneywatch.service.AccountService;

import java.util.List;

/**
 * Контроллер для добавления банков
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping
    public ResponseEntity<List<AccountDto>> findAll() {
        return
                ResponseEntity.ok(
                        accountService.findAll().stream().map(
                                accountMapper::toDto
                        ).toList()
                );
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
