package ru.otus.webbooklibrary.service;

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

    @Transactional
    @Override
    public String saveAuthor(String name) {
        final Author author = new Author(name);
        authorRepository.save(author);
        return String.format("You successfully saved a %s to repository", author.getName());
    }

    @Transactional(readOnly = true)
    @Override
    public Author getAuthorById(String id){
        return authorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Incorrect author id"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Author> getAuthorByName(String name) {
        return authorRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Transactional
    @Override
    public String updateAuthor(String id, String name) {
        final Author author = authorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Incorrect author id"));
        final String oldAuthorName = author.getName();
        author.setName(name);

        authorRepository.save(author);

        final List<Book> bookList = bookRepository.findByAuthor_Name(oldAuthorName);

        if (!bookList.isEmpty()) {
            bookList.forEach(b -> b.setAuthor(author));
            bookRepository.saveAll(bookList);
        }

        return String.format("%s was updated", name);
    }

    @Transactional
    @Override
    public String deleteAuthor(String id) {
        final Author author = authorRepository.findById(id).orElseThrow
                (() -> new IllegalArgumentException("Incorrect author id"));

        authorRepository.deleteById(id);
        bookRepository.deleteByAuthor_Name(author.getName());

        return String.format("%s was deleted", author.getName());
    }
}
