package ru.otus.spring.course.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import ru.otus.spring.course.EntityUtils;
import ru.otus.spring.course.documents.Author;
import ru.otus.spring.course.documents.Book;
import ru.otus.spring.course.documents.Comment;
import ru.otus.spring.course.documents.Style;
import ru.otus.spring.course.repository.AuthorRepository;
import ru.otus.spring.course.repository.BookRepository;
import ru.otus.spring.course.repository.CommentRepository;
import ru.otus.spring.course.repository.StyleRepository;

import java.time.Year;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"}
)
public class AppLibraryServiceTest {
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private StyleRepository styleRepository;
    @MockBean
    private CommentRepository commentRepository;
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

        when(styleRepository.findById(style.getId())).thenReturn(Optional.empty());
        when(styleRepository.save(any(Style.class))).thenReturn(style);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book actualBook = appLibraryService.createBook(book.getName(), book.getPublishedYear(), style.getId());
        assertEquals(book, actualBook);
        verify(styleRepository).findById(style.getId());
        verify(styleRepository).save(any(Style.class));
        verify(bookRepository).save(any(Book.class));
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

        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(authorRepository.save(any())).thenReturn(author);
        when(bookRepository.save(any())).thenReturn(book);

        assertTrue(appLibraryService.linkAuthorAndBook(author.getId(), book.getId()));

        verify(authorRepository).findById(author.getId());
        verify(bookRepository).findById(book.getId());
        verify(authorRepository).save(any());
        verify(bookRepository).save(any());
    }

    @Test
    void linkAuthorAndBookTest_when_author_not_exist() {
        Book book = EntityUtils.createBook();
        Author author = EntityUtils.createAuthor();

        when(authorRepository.findById(author.getId())).thenReturn(Optional.empty());
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        assertFalse(appLibraryService.linkAuthorAndBook(author.getId(), book.getId()));
        verify(authorRepository).findById(author.getId());
        verify(bookRepository).findById(book.getId());
    }

    @Test
    void linkAuthorAndBookTest_when_book_not_exist() {
        Book book = EntityUtils.createBook();
        Author author = EntityUtils.createAuthor();

        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.empty());

        assertFalse(appLibraryService.linkAuthorAndBook(author.getId(), book.getId()));
        verify(authorRepository).findById(author.getId());
        verify(bookRepository).findById(book.getId());
    }

    @Test
    void createStyleTest() {
        Style style = EntityUtils.createStyle();
        when(styleRepository.save(any(Style.class))).thenReturn(style);

        Style actualStyle = appLibraryService.createStyle(style.getId());
        assertEquals(style, actualStyle);
        verify(styleRepository).save(any(Style.class));
    }

    @Test
    void addStyleForBook_when_book_exist() {
        Book book = EntityUtils.createBook();
        Style style = EntityUtils.createStyle();

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(styleRepository.findById(style.getId())).thenReturn(Optional.empty());
        when(styleRepository.save(any())).thenReturn(style);
        when(bookRepository.save(any())).thenReturn(book);

        assertTrue(appLibraryService.addStyleForBook(book.getId(), style.getId()));
        verify(bookRepository).findById(book.getId());
        verify(styleRepository).findById(style.getId());
        verify(bookRepository).save(any());
        verify(styleRepository, times(2)).save(any());
    }

    @Test
    void addStyleForBook_when_book_does_not_exist() {
        Book book = EntityUtils.createBook();
        Style style = EntityUtils.createStyle();

        when(bookRepository.findById(any())).thenReturn(Optional.empty());

        assertFalse(appLibraryService.addStyleForBook(book.getId(), style.getId()));
        verify(bookRepository).findById(any());
        verify(styleRepository, never()).findById(style.getId());
        verify(styleRepository, never()).save(any());
    }

    @Test
    void getAllAuthorsOfBookTestWhenBookExist() {
        Book book = EntityUtils.createBookWithSomeAuthorsAndStyles();
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        assertEquals(book.getAuthors(), appLibraryService.getAllAuthorsOfBook(book.getId()));
        verify(bookRepository).findById(book.getId());
    }

    @Test
    void getAllAuthorsOfBookTestWhenBookDoesNotExist() {
        String isbn = EntityUtils.rdmString(10);
        when(bookRepository.findById(isbn)).thenReturn(Optional.empty());

        assertTrue(appLibraryService.getAllAuthorsOfBook(isbn).isEmpty());
        verify(bookRepository).findById(isbn);
    }

    @Test
    void getAllBooksByAuthorWhenAuthorExist() {
        Author author = EntityUtils.createAuthorWithBooks();
        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));

        assertEquals(author.getBooks(), appLibraryService.getAllBooksByAuthor(author.getId()));
        verify(authorRepository).findById(author.getId());
    }

    @Test
    void getAllBooksByAuthorWhenAuthorDoesNotExist() {
        String authorId = EntityUtils.rdmString(10);
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        assertTrue(appLibraryService.getAllBooksByAuthor(authorId).isEmpty());
        verify(authorRepository).findById(authorId);
    }

    @Test
    void createCommentWhenBookExist() {
        String isbn = EntityUtils.rdmString(10);
        Book book = EntityUtils.createBook();
        Comment comment = EntityUtils.createComment(book);
        when(bookRepository.findById(isbn)).thenReturn(Optional.of(book));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        assertEquals(comment, appLibraryService.addCommentToBook(isbn, comment.getText()));
        verify(bookRepository).findById(isbn);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void getBooksByPublishedYearGreaterThan() {
        Book book = EntityUtils.createBook();
        Year year = book.getPublishedYear().minusYears(1);
        Set<Book> books = new HashSet<>() {{
            add(book);
        }};
        when(bookRepository.findByPublishedYearGreaterThan(year.getValue())).thenReturn(books);

        assertEquals(books, appLibraryService.getBooksByPublishedYearGreaterThan(year));
        verify(bookRepository).findByPublishedYearGreaterThan(year.getValue());
    }

    @Test
    void getBooksWhereAuthorsMoreThan() {
        Set<Book> books = new HashSet<>() {{
            add(EntityUtils.createBook());
        }};
        when(bookRepository.findBooksWhereAuthorsMoreThan(2)).thenReturn(books);

        assertEquals(books, appLibraryService.getBooksWhereAuthorsMoreThan(2));
        verify(bookRepository).findBooksWhereAuthorsMoreThan(2);
    }

    @Test
    void getAuthorsWhereBooksMoreThan() {
        Set<Author> authors = new HashSet<>() {{
            add(EntityUtils.createAuthor());
        }};
        when(authorRepository.findAuthorsWhereBooksMoreThan(2)).thenReturn(authors);

        assertEquals(authors, appLibraryService.getAuthorsWhereBooksMoreThan(2));
        verify(authorRepository).findAuthorsWhereBooksMoreThan(2);
    }

    @Test
    void getAuthorsByName() {
        Author author = EntityUtils.createAuthor();
        Set<Author> authors = new HashSet<>() {{
            add(author);
        }};
        when(authorRepository.findAuthorsByName(author.getName())).thenReturn(authors);

        assertEquals(authors, appLibraryService.getAuthorsByName(author.getName()));
        verify(authorRepository).findAuthorsByName(author.getName());
    }

    @Test
    void getAuthorsBySurname() {
        Author author = EntityUtils.createAuthor();
        Set<Author> authors = new HashSet<>() {{
            add(author);
        }};
        when(authorRepository.findAuthorsBySurname(author.getSurname())).thenReturn(authors);

        assertEquals(authors, appLibraryService.getAuthorsBySurname(author.getSurname()));
        verify(authorRepository).findAuthorsBySurname(author.getSurname());
    }
}

