package ru.otus.spring.course.batch.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.spring.course.documents.BookDocument;
import ru.otus.spring.course.documents.CommentDocument;
import ru.otus.spring.course.entities.Comment;
import ru.otus.spring.course.repository.mongo.BookRepositoryMongo;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentProcessor implements ItemProcessor<Comment, CommentDocument> {
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
    public CommentDocument process(@NonNull Comment item) throws Exception {
        CommentDocument commentDocument = new CommentDocument()
                .setText(item.getText());
        Optional<BookDocument> optionalBookDocument = bookRepositoryMongo
                .findByNameAndPublishedYear(item.getBook().getName(), item.getBook().getPublishedYear().getValue());
        optionalBookDocument.ifPresent(commentDocument::setBook);
        return commentDocument;
    }
}
