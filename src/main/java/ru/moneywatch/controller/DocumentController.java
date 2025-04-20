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
import ru.moneywatch.model.dtos.DocumentDto;
import ru.moneywatch.service.DocumentService;

import java.util.List;

/**
 * Контроллер для работы с документами.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/api//docs/")
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping
    public List<DocumentDto> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    @GetMapping("/{id}")
    public DocumentDto getDocumentById(@PathVariable Long id) {
        return documentService.getDocumentById(id);
    }

    @PostMapping
    public DocumentDto createDocument(@RequestBody DocumentDto document) {
        return documentService.createDocument(document);
    }

    @PostMapping("/{id}")
    public DocumentDto editDocument(@PathVariable Long id, @RequestBody DocumentDto document) {
        return documentService.editDocument(id, document);
    }

    @DeleteMapping
    public void deleteDocumentById(@RequestParam Long id) {
        documentService.deleteDocumentById(id);
    }

}
