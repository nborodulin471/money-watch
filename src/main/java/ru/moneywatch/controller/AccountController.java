package ru.moneywatch.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.moneywatch.model.dtos.AccountDto;
import ru.moneywatch.model.mappers.AccountMapper;
import ru.moneywatch.service.AccountService;
import ru.moneywatch.service.auth.UserService;

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
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<AccountDto>> findAll() {
        return
                ResponseEntity.ok(
                        accountService.findAll(userService.getCurrentUser().getId()).stream().map(
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
}
