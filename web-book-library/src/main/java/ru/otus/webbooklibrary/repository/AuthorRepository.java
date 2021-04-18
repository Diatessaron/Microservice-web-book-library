package ru.otus.webbooklibrary.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.webbooklibrary.domain.Author;

import java.util.List;

public interface AuthorRepository extends MongoRepository<Author, String> {
    List<Author> findByName(String name);

    void deleteByName(String name);
}
