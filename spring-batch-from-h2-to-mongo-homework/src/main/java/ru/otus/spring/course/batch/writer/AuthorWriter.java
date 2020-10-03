package ru.otus.spring.course.batch.writer;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import ru.otus.spring.course.documents.AuthorDocument;
import ru.otus.spring.course.repository.mongo.AuthorRepositoryMongo;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorWriter implements ItemWriter<AuthorDocument> {

    private final AuthorRepositoryMongo authorRepositoryMongo;

    /**
     * Process the supplied data element. Will not be called with any null items
     * in normal operation.
     *
     * @param items items to be written
     * @throws Exception if there are errors. The framework will catch the
     *                   exception and convert or rethrow it as appropriate.
     */
    @Override
    public void write(List<? extends AuthorDocument> items) throws Exception {
        items.forEach(authorDocument -> {
            Optional<AuthorDocument> optionalAuthorDocument = authorRepositoryMongo.findByNameAndSurname(authorDocument.getName(), authorDocument.getSurname());
            if(optionalAuthorDocument.isEmpty())
                authorRepositoryMongo.save(authorDocument);
            else {
                AuthorDocument updatedAuthorDocument = optionalAuthorDocument.get();
                updatedAuthorDocument.setBooks(authorDocument.getBooks());
                authorRepositoryMongo.save(updatedAuthorDocument);
            }
        });
    }
}
