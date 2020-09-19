package ru.otus.spring.course.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.course.documents.Author;
import ru.otus.spring.course.documents.Book;
import ru.otus.spring.course.repository.AuthorRepository;
import ru.otus.spring.course.repository.BookRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Override
    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Optional<Author> getAuthorById(String authorId) {
        return authorRepository.findById(authorId);
    }

    @Override
    public void deleteById(String authorId) {
        Optional<Author> optionalAuthor = getAuthorById(authorId);
        if(optionalAuthor.isPresent()) {
            Set<Book> books = optionalAuthor.get().getBooks();
            for (Book book: books) {
                book.getAuthors().remove(optionalAuthor.get());
                bookRepository.save(book);
            }
            authorRepository.deleteById(authorId);
        }
    }

    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author createAuthor(String name, String surname) {
        return authorRepository.save(new Author()
                .setName(name)
                .setSurname(surname));
    }

    @Override
    public Set<Book> getAllBooksByAuthor(String authorId) {
        Optional<Author> optionalAuthor = authorRepository.findById(authorId);
        if (optionalAuthor.isPresent())
            return optionalAuthor.get().getBooks();
        else
            return new HashSet<>();
    }

    @Override
    public Set<Author> getAuthorsWhereBooksEquals(int count) {
        return authorRepository.findAuthorsWhereBooksEquals(count);
    }

    @Override
    public Set<Author> getAuthorsByName(String name) {
        return authorRepository.findAuthorsByName(name);
    }

    @Override
    public Set<Author> getAuthorsBySurname(String surname) {
        return authorRepository.findAuthorsBySurname(surname);
    }
}
