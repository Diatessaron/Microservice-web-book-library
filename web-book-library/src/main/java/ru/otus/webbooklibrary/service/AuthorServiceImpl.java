package ru.otus.webbooklibrary.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.webbooklibrary.domain.Author;
import ru.otus.webbooklibrary.domain.Book;
import ru.otus.webbooklibrary.repository.AuthorRepository;
import ru.otus.webbooklibrary.repository.BookRepository;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @HystrixCommand(defaultFallback = "getEmptyStringResult")
    @Transactional
    @Override
    public String saveAuthor(String name) {
        final Author author = new Author(name);
        authorRepository.save(author);
        return String.format("You successfully saved a %s to repository", author.getName());
    }

    @HystrixCommand(defaultFallback = "getEmptyAuthorResult")
    @Transactional(readOnly = true)
    @Override
    public Author getAuthorById(String id) {
        return authorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Incorrect author id"));
    }

    @HystrixCommand(defaultFallback = "getEmptyAuthorListResult")
    @Transactional(readOnly = true)
    @Override
    public List<Author> getAuthorByName(String name) {
        return authorRepository.findByName(name);
    }

    @HystrixCommand(defaultFallback = "getEmptyAuthorListResult")
    @Transactional(readOnly = true)
    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @HystrixCommand(defaultFallback = "getEmptyStringResult")
    @Transactional
    @Override
    public String updateAuthor(String id, String name) {
        final Author author = authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect author id"));
        final String oldAuthorName = author.getName();
        author.setName(name);

        final List<Book> bookList = bookRepository.findByAuthor_Name(oldAuthorName);

        if (!bookList.isEmpty()) {
            bookList.forEach(b -> b.setAuthor(author));
            bookRepository.saveAll(bookList);
        }
        authorRepository.save(author);

        return String.format("%s was updated", name);
    }

    @HystrixCommand(defaultFallback = "getEmptyStringResult")
    @Transactional
    @Override
    public String deleteAuthor(String id) {
        final Author author = authorRepository.findById(id).orElseThrow
                (() -> new IllegalArgumentException("Incorrect author id"));

        authorRepository.deleteById(id);
        bookRepository.deleteByAuthor_Name(author.getName());

        return String.format("%s was deleted", author.getName());
    }

    public String getEmptyStringResult() {
        return "Operation can not be executed.";
    }

    public Author getEmptyAuthorResult() {
        Author author = new Author();
        author.setId("N/A");
        author.setName("N/A");

        return author;
    }

    public List<Author> getEmptyAuthorListResult() {
        return List.of();
    }
}
