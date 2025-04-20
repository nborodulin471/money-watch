package ru.moneywatch.service;

import ru.moneywatch.model.dtos.DocumentDto;

import java.util.List;

/**
 * Сервис для работы с документами.
 */
public interface DocumentService {

    /**
     * Возвращает все документы.
     */
    List<DocumentDto> getAllDocuments();

    /**
     * Получает документ по его ид.
     */
    DocumentDto getDocumentById(Long id);

    /**
     * Создает документ.
     */
    DocumentDto createDocument(DocumentDto document);

    /**
     * Редактирует документ.
     */
    DocumentDto editDocument(Long id, DocumentDto document);

    /**
     * Удаляет документ по его идентификатору.
     */
    void deleteDocumentById(Long id);

}
