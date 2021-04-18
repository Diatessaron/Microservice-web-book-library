package ru.otus.webbooklibrary.service;

import ru.otus.webbooklibrary.domain.Book;

import java.util.List;

public interface BookService {
    void saveBook(String title, String authorNameParameter, String genreNameParameter);

    Book getBookById(String id);

    List<Book> getBookByTitle(String title);

    List<Book> getBookByAuthor(String author);

    List<Book> getBookByGenre(String genre);

    Book getBookByComment(String commentId);

    List<Book> getAll();

    void updateBook(String oldBookTitle, String title, String authorNameParameter, String genreNameParameter);

    void deleteBook(String id);
}
