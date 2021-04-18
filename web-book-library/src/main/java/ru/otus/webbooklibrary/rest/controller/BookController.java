package ru.otus.webbooklibrary.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.webbooklibrary.domain.Book;
import ru.otus.webbooklibrary.rest.dto.BookRequest;
import ru.otus.webbooklibrary.service.BookService;

import java.util.List;

@RestController
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(value = "/api/books",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> save(@Validated @RequestBody BookRequest bookRequest) {
        bookService.saveBook(bookRequest.getTitle(), bookRequest.getAuthorName(), bookRequest.getGenreName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/api/books/id")
    public ResponseEntity<Book> getBookById(@RequestParam String id) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getBookById(id));
    }

    @GetMapping("/api/books/title/{title}")
    public ResponseEntity<List<Book>> getBookByTitle(@PathVariable String title) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getBookByTitle(title));
    }

    @GetMapping("/api/books/author/{author}")
    public ResponseEntity<List<Book>> getBookByAuthor(@PathVariable String author) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getBookByAuthor(author));
    }

    @GetMapping("/api/books/genre/{genre}")
    public ResponseEntity<List<Book>> getBookByGenre(@PathVariable String genre) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getBookByGenre(genre));
    }

    @GetMapping("/api/books")
    public ResponseEntity<List<Book>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getAll());
    }

    @PutMapping(value = "/api/books",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> edit(@Validated @RequestBody BookRequest bookRequest) {
        bookService.updateBook(bookRequest.getId(), bookRequest.getTitle(),
                bookRequest.getAuthorName(), bookRequest.getGenreName());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping(value = "/api/books",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteByTitle(@Validated @RequestBody BookRequest bookRequest) {
        bookService.deleteBook(bookRequest.getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
