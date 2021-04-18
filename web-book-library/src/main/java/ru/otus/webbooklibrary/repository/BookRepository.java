package ru.otus.webbooklibrary.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.webbooklibrary.domain.Book;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findByTitle(String title);

    List<Book> findByAuthor_Name(String author);

    List<Book> findByGenre_Name(String genre);

    void deleteByAuthor_Name(String author);

    void deleteByGenre_Name(String genre);
}
