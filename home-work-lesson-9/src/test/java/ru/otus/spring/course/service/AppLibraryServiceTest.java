package ru.otus.spring.course.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import ru.otus.spring.course.EntityUtils;
import ru.otus.spring.course.entities.Author;
import ru.otus.spring.course.entities.Book;
import ru.otus.spring.course.entities.Comment;
import ru.otus.spring.course.entities.Style;
import ru.otus.spring.course.repository.AuthorRepository;
import ru.otus.spring.course.repository.BookRepository;
import ru.otus.spring.course.repository.CommentRepository;
import ru.otus.spring.course.repository.StyleRepository;

import java.util.UUID;

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

        when(styleRepository.findById(style.getId())).thenReturn(null);
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

        when(authorRepository.findById(author.getId())).thenReturn(author);
        when(bookRepository.findById(book.getId())).thenReturn(book);
        doNothing().when(bookRepository).linkAuthorAndBook(author, book);

        assertTrue(appLibraryService.linkAuthorAndBook(author.getId(), book.getId()));

        verify(authorRepository).findById(author.getId());
        verify(bookRepository).findById(book.getId());
        verify(bookRepository).linkAuthorAndBook(author, book);
    }

    @Test
    void linkAuthorAndBookTest_when_author_not_exist() {
        Book book = EntityUtils.createBook();
        Author author = EntityUtils.createAuthor();

        when(authorRepository.findById(author.getId())).thenReturn(null);
        when(bookRepository.findById(book.getId())).thenReturn(book);

        assertFalse(appLibraryService.linkAuthorAndBook(author.getId(), book.getId()));
        verify(authorRepository).findById(author.getId());
        verify(bookRepository).findById(book.getId());
        verify(bookRepository, never()).linkAuthorAndBook(author, book);
    }

    @Test
    void linkAuthorAndBookTest_when_book_not_exist() {
        Book book = EntityUtils.createBook();
        Author author = EntityUtils.createAuthor();

        when(authorRepository.findById(author.getId())).thenReturn(author);
        when(bookRepository.findById(book.getId())).thenReturn(null);

        assertFalse(appLibraryService.linkAuthorAndBook(author.getId(), book.getId()));
        verify(authorRepository).findById(author.getId());
        verify(bookRepository).findById(book.getId());
        verify(bookRepository, never()).linkAuthorAndBook(author, book);
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

        when(bookRepository.findById(book.getId())).thenReturn(book);
        when(styleRepository.findById(style.getId())).thenReturn(null);
        when(styleRepository.save(any())).thenReturn(style);
        doNothing().when(bookRepository).linkBookAndStyle(book, style);

        assertTrue(appLibraryService.addStyleForBook(book.getId(), style.getId()));
        verify(bookRepository).findById(book.getId());
        verify(styleRepository).findById(style.getId());
        verify(styleRepository).save(any());
        verify(bookRepository).linkBookAndStyle(book, style);
    }

    @Test
    void addStyleForBook_when_book_does_not_exist() {
        Book book = EntityUtils.createBook();
        Style style = EntityUtils.createStyle();

        when(bookRepository.findById(any())).thenReturn(null);

        assertFalse(appLibraryService.addStyleForBook(book.getId(), style.getId()));
        verify(bookRepository).findById(any());
        verify(styleRepository, never()).findById(style.getId());
        verify(styleRepository, never()).save(any());
        verify(bookRepository, never()).linkBookAndStyle(book, style);
    }

    @Test
    void getAllAuthorsOfBookTestWhenBookExist() {
        Book book = EntityUtils.createBookWithSomeAuthorsAndStyles();
        when(bookRepository.findById(book.getId())).thenReturn(book);

        assertEquals(book.getAuthors(),appLibraryService.getAllAuthorsOfBook(book.getId()));
        verify(bookRepository).findById(book.getId());
    }

    @Test
    void getAllAuthorsOfBookTestWhenBookDoesNotExist() {
        UUID isbn = UUID.randomUUID();
        when(bookRepository.findById(isbn)).thenReturn(null);

        assertTrue(appLibraryService.getAllAuthorsOfBook(isbn).isEmpty());
        verify(bookRepository).findById(isbn);
    }

    @Test
    void getAllBooksByAuthorWhenAuthorExist() {
        Author author = EntityUtils.createAuthorWithBooks();
        when(authorRepository.findById(author.getId())).thenReturn(author);

        assertEquals(author.getBooks(), appLibraryService.getAllBooksByAuthor(author.getId()));
        verify(authorRepository).findById(author.getId());
    }

    @Test
    void getAllBooksByAuthorWhenAuthorDoesNotExist() {
        UUID authorId = UUID.randomUUID();
        when(authorRepository.findById(authorId)).thenReturn(null);

        assertTrue(appLibraryService.getAllBooksByAuthor(authorId).isEmpty());
        verify(authorRepository).findById(authorId);
    }

    @Test
    void createCommentWhenBookExist() {
        UUID isbn = UUID.randomUUID();
        Book book = EntityUtils.createBook();
        Comment comment = EntityUtils.createComment(book);
        when(bookRepository.findById(isbn)).thenReturn(book);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        assertEquals(comment, appLibraryService.addCommentToBook(isbn, comment.getText()));
        verify(bookRepository).findById(isbn);
        verify(commentRepository).save(any(Comment.class));
    }
}
