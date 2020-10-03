package ru.otus.spring.course.batch.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.spring.course.documents.AuthorDocument;
import ru.otus.spring.course.documents.BookDocument;
import ru.otus.spring.course.entities.Book;
import ru.otus.spring.course.repository.mongo.AuthorRepositoryMongo;

import java.util.*;

@Component
@RequiredArgsConstructor
public class BookProcessor implements ItemProcessor<Book, BookDocument> {
    private final AuthorRepositoryMongo authorRepositoryMongo;
    /**
     * Process the provided item, returning a potentially modified or new item for continued
     * processing.  If the returned result is null, it is assumed that processing of the item
     * should not continue.
     *
     * @param item to be processed
     * @return potentially modified or new item for continued processing, {@code null} if processing of the
     * provided item should not continue.
     * @throws Exception thrown if exception occurs during processing.
     */
    @Override
    public BookDocument process(Book item) throws Exception {
        BookDocument bookDocument = new BookDocument()
                .setName(item.getName())
                .setPublishedYear(item.getPublishedYear());
        Set<AuthorDocument> authorDocuments = new HashSet<>();
        item.getAuthors().forEach(author -> {
            Optional<AuthorDocument> existedAuthor = authorRepositoryMongo.findByNameAndSurname(author.getName(), author.getSurname());
            if (existedAuthor.isPresent()) {
                authorDocuments.add(existedAuthor.get());
            } else {
                AuthorDocument authorDocument = new AuthorDocument()
                        .setName(author.getName())
                        .setSurname(author.getSurname());
                authorDocument = authorRepositoryMongo.save(authorDocument);
                authorDocuments.add(authorDocument);
            }
        });
        bookDocument.setAuthors(authorDocuments);
        return bookDocument;
    }
}
