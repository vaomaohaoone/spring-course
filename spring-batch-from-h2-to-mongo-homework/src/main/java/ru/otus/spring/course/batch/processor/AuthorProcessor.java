package ru.otus.spring.course.batch.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.spring.course.documents.AuthorDocument;
import ru.otus.spring.course.documents.BookDocument;
import ru.otus.spring.course.entities.Author;
import ru.otus.spring.course.repository.mongo.BookRepositoryMongo;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorProcessor implements ItemProcessor<Author, AuthorDocument> {
    private final BookRepositoryMongo bookRepositoryMongo;
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
    public AuthorDocument process(Author item) throws Exception {
        AuthorDocument authorDocument = new AuthorDocument().setName(item.getName()).setSurname(item.getSurname());
        item.getBooks().forEach(book -> {
            Optional<BookDocument> optionalBookDocument =bookRepositoryMongo.findByNameAndPublishedYear(book.getName(), book.getPublishedYear().getValue());
            optionalBookDocument.ifPresent(bookDocument -> authorDocument.getBooks().add(bookDocument));
        });
        return authorDocument;
    }
}
