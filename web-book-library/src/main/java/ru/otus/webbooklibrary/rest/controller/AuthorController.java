package ru.otus.webbooklibrary.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.webbooklibrary.domain.Author;
import ru.otus.webbooklibrary.rest.dto.AuthorRequest;
import ru.otus.webbooklibrary.service.AuthorService;

import java.util.List;

@RestController
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping(value = "/api/authors",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> create(@Validated  @RequestBody AuthorRequest authorRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.saveAuthor
                (authorRequest.getAuthor()));
    }

    @GetMapping("/api/authors/id")
    public ResponseEntity<Author> getAuthorById(@RequestParam String id) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.getAuthorById(id));
    }

    @GetMapping("/api/authors/{author}")
    public ResponseEntity<List<Author>> getAuthorByName(@PathVariable String author) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.getAuthorByName(author));
    }

    @GetMapping("/api/authors")
    public ResponseEntity<List<Author>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.getAll());
    }

    @PutMapping(value = "/api/authors",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> edit(@Validated @RequestBody AuthorRequest authorRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.updateAuthor
                (authorRequest.getId(), authorRequest.getAuthor()));
    }

    @DeleteMapping(value = "/api/authors",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteByName(@Validated @RequestBody AuthorRequest authorRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.deleteAuthor(authorRequest.getId()));
    }
}
