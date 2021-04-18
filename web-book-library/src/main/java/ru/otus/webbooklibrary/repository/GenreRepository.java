package ru.otus.webbooklibrary.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.webbooklibrary.domain.Genre;

import java.util.Optional;

public interface GenreRepository extends MongoRepository<Genre, String> {
    Optional<Genre> findByName(String name);

    void deleteByName(String name);
}
