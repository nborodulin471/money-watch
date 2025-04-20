package ru.moneywatch.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ru.moneywatch.model.dtos.DocumentDto;
import ru.moneywatch.model.mappers.DocumentMapper;
import ru.moneywatch.repository.DocumentRepository;
import ru.moneywatch.service.DocumentService;

import java.util.List;

/**
 * Реализация сервиса для работы с документами.
 */
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;

    @Override
    public List<DocumentDto> getAllDocuments() {
        return documentRepository.findAll().stream()
                .map(documentMapper::toDto)
                .toList();
    }

    @Override
    public DocumentDto getDocumentById(Long id) {
        var document = documentRepository.findById(id)
                .orElseThrow();

        return documentMapper.toDto(document);
    }

    @Override
    public DocumentDto createDocument(@RequestBody DocumentDto document) {
        var documentEntity = documentMapper.toEntity(document);

        return documentMapper.toDto(
                documentRepository.save(documentEntity)
        );
    }

    @Override
    public DocumentDto editDocument(Long id, DocumentDto document) {
        // Получаем документ из базы
        var actualDocument = documentRepository.findById(id)
                .orElseThrow();

        // TODO тут должна быть логика по мержу двух объектов

        return documentMapper.toDto(
                documentRepository.save(actualDocument)
        );
    }

    @Override
    public void deleteDocumentById(@RequestParam Long id) {
        documentRepository.deleteById(id);
    }
}
