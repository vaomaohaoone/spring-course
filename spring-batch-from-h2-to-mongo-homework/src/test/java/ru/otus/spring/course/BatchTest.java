package ru.otus.spring.course;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.course.documents.AuthorDocument;
import ru.otus.spring.course.documents.BookDocument;
import ru.otus.spring.course.documents.CommentDocument;
import ru.otus.spring.course.repository.mongo.AuthorRepositoryMongo;
import ru.otus.spring.course.repository.mongo.BookRepositoryMongo;
import ru.otus.spring.course.repository.mongo.CommentRepositoryMongo;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.spring.course.config.JobConfig.JOB_NAME;

@SpringBootTest
@SpringBatchTest
public class BatchTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    @Autowired
    private AuthorRepositoryMongo authorRepositoryMongo;
    @Autowired
    private BookRepositoryMongo bookRepositoryMongo;
    @Autowired
    private CommentRepositoryMongo commentRepositoryMongo;

    @BeforeEach
    void clearMetaData() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void testJob() throws Exception {
        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo(JOB_NAME);

        assertThat(authorRepositoryMongo.findAll()).isEmpty();
        assertThat(bookRepositoryMongo.findAll()).isEmpty();
        assertThat(commentRepositoryMongo.findAll()).isEmpty();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParameters());

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        List<AuthorDocument> mongoAuthors = authorRepositoryMongo.findAll();
        List<BookDocument> mongoBooks = bookRepositoryMongo.findAll();
        List<CommentDocument> mongoComments = commentRepositoryMongo.findAll();

        assertThat(mongoAuthors).isNotEmpty().hasSize(3);
        assertThat(mongoBooks).isNotEmpty().hasSize(3);
        assertThat(mongoComments).isNotEmpty().hasSize(4);

        assertThat(mongoAuthors.stream().filter(authorDocument -> authorDocument.getBooks().size() > 0).count()).isEqualTo(2);
        assertThat(mongoBooks.stream().filter(bookDocument -> bookDocument.getAuthors().size() > 0).count()).isEqualTo(2);
    }
}
