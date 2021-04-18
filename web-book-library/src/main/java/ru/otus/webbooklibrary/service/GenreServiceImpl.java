package ru.otus.webbooklibrary.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.webbooklibrary.domain.Book;
import ru.otus.webbooklibrary.domain.Genre;
import ru.otus.webbooklibrary.repository.BookRepository;
import ru.otus.webbooklibrary.repository.GenreRepository;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    public GenreServiceImpl(GenreRepository genreRepository, BookRepository bookRepository) {
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    @Override
    public String saveGenre(String name) {
        final Genre genre = new Genre(name);
        genreRepository.save(genre);

        return String.format("You successfully saved a %s to repository", genre.getName());
    }

    @Transactional(readOnly = true)
    @Override
    public Genre getGenreById(String id){
        return genreRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Incorrect genre id"));
    }

    @Transactional(readOnly = true)
    @Override
    public Genre getGenreByName(String name) {
        return genreRepository.findByName(name).orElseThrow
                (() -> new IllegalArgumentException("Incorrect name"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    @Transactional
    @Override
    public String updateGenre(String id, String name) {
        final Genre genre = genreRepository.findById(id).orElseThrow
                (() -> new IllegalArgumentException("Incorrect genre id"));
        final String oldGenreName = genre.getName();
        genre.setName(name);

        genreRepository.save(genre);

        final List<Book> bookList = bookRepository.findByGenre_Name(oldGenreName);

        if (!bookList.isEmpty()) {
            bookList.forEach(b -> b.setGenre(genre));
            bookRepository.saveAll(bookList);
        }

        return String.format("%s was updated", name);
    }

    @Transactional
    @Override
    public String deleteGenre(String id) {
        final Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect genre id"));
        genreRepository.deleteById(id);
        bookRepository.deleteByGenre_Name(genre.getName());

        return String.format("%s was deleted", genre.getName());
    }
}
