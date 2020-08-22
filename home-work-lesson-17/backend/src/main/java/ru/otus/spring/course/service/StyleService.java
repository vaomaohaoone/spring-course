package ru.otus.spring.course.service;

import ru.otus.spring.course.documents.Style;

import java.util.List;
import java.util.Optional;

public interface StyleService {
    Style saveStyle(Style style);
    Optional<Style> getStyleById(String styleId);
    void deleteById(String styleId);
    List<Style> getAll();
    Style createStyle(String style);
}
