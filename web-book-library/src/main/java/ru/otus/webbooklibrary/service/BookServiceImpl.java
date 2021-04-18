package ru.otus.webbooklibrary.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.webbooklibrary.domain.Author;
import ru.otus.webbooklibrary.domain.Book;
import ru.otus.webbooklibrary.domain.Comment;
import ru.otus.webbooklibrary.domain.Genre;
import ru.otus.webbooklibrary.repository.AuthorRepository;
import ru.otus.webbooklibrary.repository.BookRepository;
import ru.otus.webbooklibrary.repository.CommentRepository;
import ru.otus.webbooklibrary.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final CommentRepository commentRepository;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository,
                           GenreRepository genreRepository, CommentRepository commentRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    @Override
    public void saveBook(String title, String authorNameParameter, String genreNameParameter) {
        Author author = getAuthor(authorNameParameter);
        Genre genre = getGenre(genreNameParameter);
        final Book book = new Book(title, author, genre);

        bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    @Override
    public Book getBookById(String id) {
        return bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Incorrect book id"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> getBookByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> getBookByAuthor(String author) {
        return bookRepository.findByAuthor_Name(author);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> getBookByGenre(String genre) {
        return bookRepository.findByGenre_Name(genre);
    }

    @Transactional(readOnly = true)
    @Override
    public Book getBookByComment(String commentId) {
        return bookRepository.findByTitle(commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect comment id")).getBook().getTitle()).get(0);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Transactional
    @Override
    public void updateBook(String id, String title, String authorNameParameter,
                           String genreNameParameter) {
        Author author = getAuthor(authorNameParameter);
        Genre genre = getGenre(genreNameParameter);

        final Book book = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Incorrect book id"));
        final String oldBookTitle = book.getTitle();
        book.setAuthor(author);
        book.setGenre(genre);
        book.setTitle(title);

        bookRepository.save(book);

        final List<Comment> commentList = commentRepository.findByBook_Title(oldBookTitle);

        commentList.forEach(c -> c.setBook(title));
        commentRepository.saveAll(commentList);
    }

    @Transactional
    @Override
    public void deleteBook(String id) {
        commentRepository.deleteByBook_Title(bookRepository.findById(id).orElseThrow
                (() -> new IllegalArgumentException("Incorrect book id")).getTitle());
        bookRepository.deleteById(id);
    }

    private Author getAuthor(String authorName) {
        final List<Author> authors = authorRepository.findByName(authorName);

        if (authors.isEmpty()) {
            final Author author = new Author(authorName);
            authorRepository.save(author);
            return author;
        } else
            return authors.get(0);
    }

    private Genre getGenre(String genreName) {
        final Optional<Genre> optionalGenre = genreRepository.findByName(genreName);

        if (optionalGenre.isEmpty()) {
            final Genre genre = new Genre(genreName);
            genreRepository.save(genre);
            return genre;
        } else
            return optionalGenre.get();
    }
}
