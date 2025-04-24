package ru.moneywatch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ru.moneywatch.model.dtos.TransactionDto;
import ru.moneywatch.service.TransactionService;

import java.util.List;

/**
 * Контроллер для работы с документами.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/api//docs/")
public class TransactionController {

    private final TransactionService documentService;

    @GetMapping
    public List<TransactionDto> getAllDocuments() {
        return documentService.getAll();
    }

    @GetMapping("/{id}")
    public TransactionDto getDocumentById(@PathVariable Long id) {
        return documentService.getById(id);
    }

    @PostMapping
    public TransactionDto createDocument(@RequestBody TransactionDto document) {
        return documentService.create(document);
    }

    @PostMapping("/{id}")
    public TransactionDto editDocument(@PathVariable Long id, @RequestBody TransactionDto document) {
        return documentService.edit(id, document);
    }

    @DeleteMapping
    public void deleteDocumentById(@RequestParam Long id) {
        documentService.deleteById(id);
    }

}
