package ru.otus.webbooklibrary.service;

import ru.otus.webbooklibrary.domain.Author;

import java.util.List;

public interface AuthorService {
    String saveAuthor(String name);

    Author getAuthorById(String id);

    List<Author> getAuthorByName(String name);

    List<Author> getAll();

    String updateAuthor(String id, String name);

    String deleteAuthor(String id);
}
