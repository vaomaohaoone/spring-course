package ru.otus.spring.course.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import ru.otus.spring.course.entities.links.AuthorBook;
import ru.otus.spring.course.entities.links.BookStyle;
import ru.otus.spring.course.EntityUtils;
import ru.otus.spring.course.entities.Author;
import ru.otus.spring.course.entities.Book;
import ru.otus.spring.course.entities.Style;
import ru.otus.spring.course.repository.*;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"},
        classes = AppLibraryServiceTest.AppLibraryServiceTestConfig.class)
public class AppLibraryServiceTest {
    @MockBean
    private DataSource dataSource;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private StyleRepository styleRepository;
    @MockBean
    private BookStyleRepository bookStyleRepository;
    @MockBean
    private AuthorBookRepository authorBookRepository;
    @Autowired
    private AppLibraryService appLibraryService;

    @Test
    void simpleCreateBookTest() {
        Book book = EntityUtils.createBook();
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book actualBook = appLibraryService.createBook(book.getName(), book.getPublishedYear());
        assertEquals(book, actualBook);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void createBookTest() {
        Book book = EntityUtils.createBook();
        Style style = EntityUtils.createStyle();
        BookStyle bookStyle = new BookStyle(book.getIsbn(), style.getName());

        when(styleRepository.findById(style.getName())).thenReturn(Optional.empty());
        when(styleRepository.save(any(Style.class))).thenReturn(style);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookStyleRepository.save(any(BookStyle.class))).thenReturn(bookStyle);

        Book actualBook = appLibraryService.createBook(book.getName(), book.getPublishedYear(), style.getName());
        assertEquals(book, actualBook);
        verify(styleRepository).findById(style.getName());
        verify(styleRepository).save(any(Style.class));
        verify(bookRepository).save(any(Book.class));
        verify(bookStyleRepository).save(any(BookStyle.class));
    }

    @Test
    void createAuthorTest() {
        Author author = EntityUtils.createAuthor();
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        Author actualAuthor = appLibraryService.createAuthor(author.getName(), author.getSurname());
        assertEquals(author, actualAuthor);
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    void linkAuthorAndBookTest_when_author_and_book_exist() {
        Book book = EntityUtils.createBook();
        Author author = EntityUtils.createAuthor();
        AuthorBook authorBook = new AuthorBook(author.getId(), book.getIsbn());

        when(authorRepository.findById(any())).thenReturn(Optional.of(author));
        when(bookRepository.findById(any())).thenReturn(Optional.of(book));
        when(authorBookRepository.save(any(AuthorBook.class))).thenReturn(authorBook);

        assertTrue(appLibraryService.linkAuthorAndBook(author.getId(), book.getIsbn()));

        verify(authorRepository).findById(any());
        verify(bookRepository).findById(any());
        verify(authorBookRepository).save(any(AuthorBook.class));
    }

    @Test
    void linkAuthorAndBookTest_when_author_not_exist() {
        Book book = EntityUtils.createBook();
        Author author = EntityUtils.createAuthor();

        when(authorRepository.findById(any())).thenReturn(Optional.empty());

        assertFalse(appLibraryService.linkAuthorAndBook(author.getId(), book.getIsbn()));
        verify(authorRepository).findById(any());
    }

    @Test
    void linkAuthorAndBookTest_when_book_not_exist() {
        Book book = EntityUtils.createBook();
        Author author = EntityUtils.createAuthor();

        when(authorRepository.findById(any())).thenReturn(Optional.of(author));
        when(bookRepository.findById(any())).thenReturn(Optional.empty());

        assertFalse(appLibraryService.linkAuthorAndBook(author.getId(), book.getIsbn()));
        verify(bookRepository).findById(any());
    }

    @Test
    void addStyleForBook_when_book_exist() {
        Book book = EntityUtils.createBook();
        Style style = EntityUtils.createStyle();
        BookStyle bookStyle = new BookStyle(book.getIsbn(), style.getName());

        when(bookRepository.findById(any())).thenReturn(Optional.of(book));
        when(styleRepository.findById(any())).thenReturn(Optional.empty());
        when(styleRepository.save(any())).thenReturn(style);
        when(bookStyleRepository.save(any())).thenReturn(bookStyle);

        assertTrue(appLibraryService.addStyleForBook(book.getIsbn(), style.getName()));
        verify(bookRepository).findById(any());
        verify(styleRepository).findById(any());
        verify(styleRepository).save(any());
        verify(bookStyleRepository).save(any());
    }

    @Test
    void addStyleForBook_when_book_does_not_exist() {
        Book book = EntityUtils.createBook();
        Style style = EntityUtils.createStyle();

        when(bookRepository.findById(any())).thenReturn(Optional.empty());

        assertFalse(appLibraryService.addStyleForBook(book.getIsbn(), style.getName()));
        verify(bookRepository).findById(any());
    }

    @Test
    void createStyleTest() {
        Style style = EntityUtils.createStyle();
        when(styleRepository.save(any(Style.class))).thenReturn(style);

        Style actualStyle = appLibraryService.createStyle(style.getName());
        assertEquals(style, actualStyle);
        verify(styleRepository).save(any(Style.class));
    }

    @Test
    void getAllAuthorsOfBookTest() {
        UUID isbn = UUID.randomUUID();
        List<Author> authors = new ArrayList<>(){{
            add(EntityUtils.createAuthor());
            add(EntityUtils.createAuthor());
        }};
        when(authorBookRepository.findAllAuthorsOfBook(isbn)).thenReturn(authors);

        List<Author> actualAuthors = appLibraryService.getAllAuthorsOfBook(isbn);
        assertEquals(authors, actualAuthors);
    }

    @Test
    void getAllBooksByAuthor() {
        UUID authorId = UUID.randomUUID();
        List<Book> books = new ArrayList<>(){{
            add(EntityUtils.createBook());
            add(EntityUtils.createBook());
        }};
        when(authorBookRepository.findAllBooksForByAuthor(authorId)).thenReturn(books);

        List<Book> actualBooks = appLibraryService.getAllBooksByAuthor(authorId);
        assertEquals(books, actualBooks);
    }

    @TestConfiguration
    public static class AppLibraryServiceTestConfig {
        @Bean
        public DataSource dataSource(){
            return mock(DataSource.class);
        }
    }
}
