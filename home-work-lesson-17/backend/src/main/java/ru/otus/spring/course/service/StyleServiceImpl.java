package ru.otus.spring.course.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.course.documents.Book;
import ru.otus.spring.course.documents.Style;
import ru.otus.spring.course.repository.BookRepository;
import ru.otus.spring.course.repository.StyleRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class StyleServiceImpl implements StyleService {

    private final StyleRepository styleRepository;
    private final BookRepository bookRepository;

    @Override
    public Style saveStyle(Style style) {
        return styleRepository.save(style);
    }

    @Override
    public Optional<Style> getStyleById(String styleId) {
        return styleRepository.findById(styleId);
    }

    @Override
    public void deleteById(String styleId) {
        Optional<Style> optionalStyle = getStyleById(styleId);
        if(optionalStyle.isPresent()) {
            Set<Book> books = optionalStyle.get().getBooks();
            for (Book book: books) {
                book.getStyles().remove(optionalStyle.get());
                bookRepository.save(book);
            }
            styleRepository.deleteById(styleId);
        }
    }

    @Override
    public List<Style> getAll() {
        return styleRepository.findAll();
    }

    @Override
    public Style createStyle(String style) {
        return styleRepository.save(new Style()
                .setStyle(style));
    }
}
