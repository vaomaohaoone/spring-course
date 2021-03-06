package ru.otus.spring.course.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.course.documents.Author;
import ru.otus.spring.course.documents.Book;
import ru.otus.spring.course.documents.Comment;
import ru.otus.spring.course.documents.Style;
import ru.otus.spring.course.service.AppLibraryService;

import java.time.Year;
import java.util.List;
import java.util.Set;

@ShellComponent
@RequiredArgsConstructor
public class AppShell {
    private final AppLibraryService appLibraryService;

    @ShellMethod(key = {"create-author", "ca"}, value = "create author command")
    public void createAuthor(@ShellOption String name, @ShellOption String surname) {
        Author author = appLibraryService.createAuthor(name, surname);
        System.out.println("The author with name: " + author.getName() + ", surname: " + author.getSurname() + " and id: " + author.getId() + " was created");
    }

    @ShellMethod(key = {"create-book", "cb"}, value = "create book simple")
    public void createBook(@ShellOption String name, @ShellOption int year) {
        Year publishedYear = Year.of(year);
        Book book = appLibraryService.createBook(name, publishedYear);
        System.out.println("The book with name: " + book.getName() + ", published year: " + book.getPublishedYear() + " and isbn: " + book.getId() + " was created");
    }

    @ShellMethod(key = {"create-book-with-style", "cbs"}, value = "create book with style")
    public void createBookWithStyle(@ShellOption String name, @ShellOption int year, @ShellOption String style) {
        Year publishedYear = Year.of(year);
        Book book = appLibraryService.createBook(name, publishedYear, style);
        System.out.println("The book with name: " + book.getName() + ", published year: " + book.getPublishedYear() + " and isbn: " + book.getId() + " was created");
    }

    @ShellMethod(key = {"create-style", "cs"}, value = "create style")
    public void createStyle(@ShellOption String style) {
        Style styleEntity = appLibraryService.createStyle(style);
        System.out.println("Style with name: " + styleEntity.getId() + " was created");
    }

    @ShellMethod(key = {"link-author-and-book", "abl"}, value = "link author and book by author id and isbn book")
    public void linkAuthorAndBook(@ShellOption String authorId, @ShellOption String isbn) {
        if (appLibraryService.linkAuthorAndBook(authorId, isbn))
            System.out.println("Author with id: " + authorId + " and book with isbn: " + isbn + " was linked");
        else
            System.out.println("Author with id: " + authorId + " or book with isbn: " + isbn + " does not exist");
    }

    @ShellMethod(key = {"add-style-for-book", "asb"}, value = "Add style for book by style name and isbn book")
    public void addStyleForBook(@ShellOption String style, @ShellOption String isbn) {
        if (appLibraryService.addStyleForBook(isbn, style))
            System.out.println("Style with name: " + style + " was added to book with isbn: " + isbn);
        else
            System.out.println("No such book with isbn: " + isbn);
    }

    @Transactional
    @ShellMethod(key = {"get-all-authors-for-book", "get-authors"}, value = "Get all authors for book by isbn")
    public void getAllAuthorsOfBook(@ShellOption String isbn) {
        Set<Author> authors = appLibraryService.getAllAuthorsOfBook(isbn);
        System.out.println("Authors for book with isbn: " + isbn);
        for (Author author : authors) {
            System.out.println("Author with name: " + author.getName() + " " + author.getSurname() + " and id: " + author.getId());
        }
    }

    @Transactional
    @ShellMethod(key = {"get-all-books-by-author-id", "get-books"}, value = "Get all bookds by author id")
    public void getAllBooksByAuthor(@ShellOption String authorId) {
        Set<Book> books = appLibraryService.getAllBooksByAuthor(authorId);
        System.out.println("Books fot author with id: " + authorId);
        for (Book book : books) {
            System.out.println("Book with name: " + book.getName() + ", published year: " + book.getPublishedYear() + ", isbn: " + book.getId());
        }
    }

    @ShellMethod(key = {"create-comment"}, value = "Create comment for book")
    public void createComment(@ShellOption String isbn, @ShellOption String text) {
        Comment comment = appLibraryService.addCommentToBook(isbn, text);
        if (comment != null)
            System.out.println("Comment with id: " + comment.getId() + ", text: " + comment.getText() + ", for book with isbn: " + isbn + " was added");
        else
            System.out.println("No such book with isbn: " + isbn);
    }

    @ShellMethod(key = {"books-by-year-greater-than", "bbyear"}, value = "Get books by year greater than")
    public void getBooksByPublishedYearGreaterThan(@ShellOption int year) {
        Year publishedYear = Year.of(year);
        Set<Book> books = appLibraryService.getBooksByPublishedYearGreaterThan(publishedYear);
        System.out.println("Books:");
        for (Book book : books) {
            System.out.println("Book with name: " + book.getName() + ", published year: " + book.getPublishedYear() + ", isbn: " + book.getId());
        }
    }

    @ShellMethod(key = {"get-books-where-authors-more-than", "gbwam"}, value = "Get books where authors count more than")
    public void getBooksWhereAuthorsMoreThan(@ShellOption int count) {
        Set<Book> books = appLibraryService.getBooksWhereAuthorsMoreThan(count);
        System.out.println("Books:");
        for (Book book : books) {
            System.out.println("Book with name: " + book.getName() + ", published year: " + book.getPublishedYear() + ", isbn: " + book.getId());
        }
    }

    @ShellMethod(key = {"get-authors-where-books-more-than", "gawbm"}, value = "Get authors where books count more than")
    public void getAuthorsWhereBooksMoreThan(@ShellOption int count) {
        Set<Author> authors = appLibraryService.getAuthorsWhereBooksMoreThan(count);
        System.out.println("Authors:");
        for (Author author : authors) {
            System.out.println("Author with name: " + author.getName() + " " + author.getSurname() + " and id: " + author.getId());
        }
    }

    @ShellMethod(key = {"get-authors-by-name"}, value = "Get authors by name")
    public void getAuthorsByName(@ShellOption String name) {
        Set<Author> authors = appLibraryService.getAuthorsByName(name);
        System.out.println("Authors:");
        for (Author author : authors) {
            System.out.println("Author with name: " + author.getName() + " " + author.getSurname() + " and id: " + author.getId());
        }
    }

    @ShellMethod(key = {"get-authors-by-surname"}, value = "Get authors by surname")
    public void getAuthorsBySurname(@ShellOption String surname) {
        Set<Author> authors = appLibraryService.getAuthorsBySurname(surname);
        System.out.println("Authors:");
        for (Author author : authors) {
            System.out.println("Author with name: " + author.getName() + " " + author.getSurname() + " and id: " + author.getId());
        }
    }
}