package ru.otus.spring.course.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import ru.otus.spring.course.documents.AuthorDocument;
import ru.otus.spring.course.documents.BookDocument;
import ru.otus.spring.course.documents.CommentDocument;
import ru.otus.spring.course.entities.Author;
import ru.otus.spring.course.entities.Book;
import ru.otus.spring.course.entities.Comment;
import ru.otus.spring.course.repository.h2.AuthorRepositoryH2;
import ru.otus.spring.course.repository.h2.BookRepositoryH2;
import ru.otus.spring.course.repository.h2.CommentRepositoryH2;
import ru.otus.spring.course.repository.mongo.AuthorRepositoryMongo;
import ru.otus.spring.course.repository.mongo.BookRepositoryMongo;
import ru.otus.spring.course.repository.mongo.CommentRepositoryMongo;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Configuration
@RequiredArgsConstructor
public class JobConfig {
    private static final int CHUNK_SIZE = 5;
    public static final String JOB_NAME = "fromH2ToMongoJob";
    private final Logger logger = LoggerFactory.getLogger("Batch");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job importUserJob(Step setup, Step migrateBooks, Step migrateAuthors, Step migrateComments) {
        return jobBuilderFactory.get(JOB_NAME)
                .flow(setup)
                .next(migrateBooks)
                .next(migrateAuthors)
                .next(migrateComments)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        logger.info("Начало job");
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        logger.info("Конец job");
                    }
                })
                .build();
    }

    @Bean
    public Step setup(BookRepositoryMongo bookRepositoryMongo, AuthorRepositoryMongo authorRepositoryMongo,
                      CommentRepositoryMongo commentRepositoryMongo) {
        return stepBuilderFactory.get("clean-documents").tasklet((stepContribution, chunkContext) -> {
            commentRepositoryMongo.deleteAll();
            authorRepositoryMongo.deleteAll();
            bookRepositoryMongo.deleteAll();
            return RepeatStatus.FINISHED;
        }).build();
    }


    @Bean
    public Step migrateBooks(ItemReader<Book> bookReader, ItemWriter<BookDocument> bookWriter, ItemProcessor<Book, BookDocument> bookProcessor) {
        return stepBuilderFactory.get("migrateBooks")
                .<Book, BookDocument>chunk(CHUNK_SIZE)
                .reader(bookReader)
                .processor(bookProcessor)
                .writer(bookWriter)
                .listener(new ItemReadListener<>() {
                    @Override
                    public void beforeRead() {
                        logger.info("Начало чтения книг");
                    }

                    @Override
                    public void afterRead(@NonNull Book book) {
                        logger.info("Книга : " + book.toString() + " считана успешно");
                    }

                    @Override
                    public void onReadError(@NonNull Exception e) {
                        logger.error("Ошибка чтения книги: " + e.getMessage());
                    }
                })
                .listener(new ItemWriteListener<BookDocument>() {
                    @Override
                    public void beforeWrite(@NonNull List<? extends BookDocument> list) {
                        logger.info("Начало записи книг: " + list.stream().map(BookDocument::toString).collect(Collectors.joining(",")));
                    }

                    @Override
                    public void afterWrite(@NonNull List<? extends BookDocument> list) {
                        logger.info("Запись книг прошла успешно!");
                    }

                    @Override
                    public void onWriteError(@NonNull Exception e, @NonNull List<? extends BookDocument> list) {
                        logger.error("Ошибка записи книг " + list.stream().map(BookDocument::toString).collect(Collectors.joining(",")) + ": " + e.getMessage());
                    }
                })
                .build();
    }

    @Bean
    public Step migrateAuthors(ItemReader<Author> authorReader, ItemWriter<AuthorDocument> authorWriter, ItemProcessor<Author, AuthorDocument> authorProcessor) {
        return stepBuilderFactory.get("migrateAuthors")
                .<Author, AuthorDocument>chunk(CHUNK_SIZE)
                .reader(authorReader)
                .processor(authorProcessor)
                .writer(authorWriter)
                .build();
    }

    @Bean
    public Step migrateComments(ItemReader<Comment> commentReader, ItemWriter<CommentDocument> commentWriter, ItemProcessor<Comment, CommentDocument> commentProcessor) {
        return stepBuilderFactory.get("migrateComments")
                .<Comment, CommentDocument>chunk(CHUNK_SIZE)
                .reader(commentReader)
                .processor(commentProcessor)
                .writer(commentWriter)
                .build();
    }

    @Bean
    public RepositoryItemWriter<BookDocument> bookWriter(BookRepositoryMongo bookRepositoryMongo) {
        return new RepositoryItemWriterBuilder<BookDocument>()
                .repository(bookRepositoryMongo)
                .methodName("save")
                .build();
    }

    @Bean
    public RepositoryItemWriter<CommentDocument> commentWriter(CommentRepositoryMongo commentRepositoryMongo) {
        return new RepositoryItemWriterBuilder<CommentDocument>()
                .repository(commentRepositoryMongo)
                .methodName("save")
                .build();
    }

    @Bean
    public RepositoryItemReader<Book> bookReader(BookRepositoryH2 bookRepositoryH2) {
        return new RepositoryItemReaderBuilder<Book>()
                .repository(bookRepositoryH2)
                .methodName("findAll")
                .name("findAll")
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public RepositoryItemReader<Author> authorReader(AuthorRepositoryH2 authorRepositoryH2) {
        return new RepositoryItemReaderBuilder<Author>()
                .repository(authorRepositoryH2)
                .methodName("findAll")
                .name("findAll")
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public RepositoryItemReader<Comment> commentReader(CommentRepositoryH2 commentRepositoryH2) {
        return new RepositoryItemReaderBuilder<Comment>()
                .repository(commentRepositoryH2)
                .methodName("findAll")
                .name("findAll")
                .sorts(new HashMap<>())
                .build();
    }
}
