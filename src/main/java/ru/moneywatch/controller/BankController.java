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
import ru.moneywatch.model.dtos.BankDto;
import ru.moneywatch.model.mappers.BankMapper;
import ru.moneywatch.service.BankService;

import java.util.List;

/**
 * Контроллер для добавления банков
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bank")
public class BankController {

    private final BankService bankService;
    private final BankMapper bankMapper;

    @GetMapping
    public ResponseEntity<List<BankDto>> findAll() {
        return
                ResponseEntity.ok(
                        bankService.findAll().stream().map(
                                bankMapper::toDto
                        ).toList()
                );
    }

    @PostMapping
    public ResponseEntity<BankDto> create(@RequestBody BankDto bankDto) {
        return
                ResponseEntity.ok(
                        bankMapper.toDto(bankService.createBank(bankDto))
                );
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bankService.delete(id);
    }

}
