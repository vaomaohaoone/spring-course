package ru.otus.spring.course.shell;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.course.documents.AuthorDocument;
import ru.otus.spring.course.documents.BookDocument;
import ru.otus.spring.course.entities.Author;
import ru.otus.spring.course.entities.Book;
import ru.otus.spring.course.repository.h2.AuthorRepositoryH2;
import ru.otus.spring.course.repository.h2.BookRepositoryH2;
import ru.otus.spring.course.repository.mongo.AuthorRepositoryMongo;
import ru.otus.spring.course.repository.mongo.BookRepositoryMongo;

import java.util.List;

@RequiredArgsConstructor
@ShellComponent
@Slf4j
public class BatchLaunch {
    private final JobLauncher jobLauncher;
    private final Job migrateBooksJob;
    private final BookRepositoryH2 bookRepositoryH2;
    private final BookRepositoryMongo bookRepositoryMongo;
    private final AuthorRepositoryH2 authorRepositoryH2;
    private final AuthorRepositoryMongo authorRepositoryMongo;
    private final static String MONGO_SOURCE = "mongo";
    private final static String H2_SOURCE = "h2";

    @ShellMethod(key = {"run-migration"}, value = "run spring batch migration job")
    public void startMigration() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        jobLauncher.run(migrateBooksJob, new JobParametersBuilder().toJobParameters());
    }

    @Transactional
    @ShellMethod(key = {"find-all-books"}, value = "find all books")
    public void findAllBooks(@ShellOption String source) {
        if (source.equals(H2_SOURCE)) {
            List<Book> books = bookRepositoryH2.findAll();
            for (Book book : books) {
                System.out.println("Book with name: " + book.getName() + ", published year: " + book.getPublishedYear() + ", isbn: " + book.getId() + " has authors:");
                for (Author author : book.getAuthors()) {
                    System.out.println("\tAuthor with name: " + author.getName() + " " + author.getSurname() + " and id: " + author.getId());
                }
            }
        } else if (source.equals(MONGO_SOURCE)) {
            List<BookDocument> bookDocuments = bookRepositoryMongo.findAll();
            for (BookDocument bookDocument : bookDocuments) {
                System.out.println("Book with name: " + bookDocument.getName() + ", published year: " + bookDocument.getPublishedYear() + ", isbn: " + bookDocument.getId() + " has authors:");
                for (AuthorDocument authorDocument : bookDocument.getAuthors()) {
                    System.out.println("\tAuthor with name: " + authorDocument.getName() + " " + authorDocument.getSurname() + " and id: " + authorDocument.getId());
                }
            }
        } else
            log.info(String.format("Invalid option: %s, available - %s or %s", source, H2_SOURCE, MONGO_SOURCE));
    }

    @Transactional
    @ShellMethod(key = {"find-all-authors"}, value = "find all authors")
    public void findAllAuthors(@ShellOption String source) {
        if (source.equals(H2_SOURCE)) {
            Iterable<Author> authors = authorRepositoryH2.findAll();
            for (Author author : authors) {
                System.out.println("Author with name: " + author.getName() + " " + author.getSurname() + " and id: " + author.getId() + " has books:");
                for (Book book : author.getBooks())
                    System.out.println("\tBook with name: " + book.getName() + ", published year: " + book.getPublishedYear() + ", isbn: " + book.getId());
            }
        } else if (source.equals(MONGO_SOURCE)) {
            List<AuthorDocument> authorDocuments = authorRepositoryMongo.findAll();
            for (AuthorDocument authorDocument : authorDocuments) {
                System.out.println("Author with name: " + authorDocument.getName() + " " + authorDocument.getSurname() + " and id: " + authorDocument.getId() + " has books:");
                for (BookDocument bookDocument : authorDocument.getBooks())
                    System.out.println("\tBook with name: " + bookDocument.getName() + ", published year: " + bookDocument.getPublishedYear() + ", isbn: " + bookDocument.getId());
            }
        } else
            log.info(String.format("Invalid option: %s, available - %s or %s", source, H2_SOURCE, MONGO_SOURCE));
    }
}
