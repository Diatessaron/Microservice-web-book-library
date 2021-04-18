package ru.otus.webbooklibrary.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.webbooklibrary.domain.Author;
import ru.otus.webbooklibrary.domain.Book;
import ru.otus.webbooklibrary.domain.Genre;
import ru.otus.webbooklibrary.rest.dto.BookRequest;
import ru.otus.webbooklibrary.service.BookServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookServiceImpl bookService;

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Test
    void testCreateByStatus() throws Exception {
        final Book book = new Book("Modernist novel",
                new Author("James Joyce"), new Genre("Modernist novel"));

        BookRequest bookRequest = new BookRequest();
        bookRequest.setTitle(book.getTitle());
        bookRequest.setAuthorName(book.getAuthor().getName());
        bookRequest.setGenreName(book.getGenre().getName());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(bookRequest);

        mockMvc.perform(post("/api/books").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetBookByTitleByStatus() throws Exception {
        when(bookService.getBookByTitle("Book")).thenReturn(List.of(new Book("Book", new Author("Author"),
                new Genre("Genre"))));

        mockMvc.perform(get("/api/books/title/Book"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllByStatus() throws Exception {
        when(bookService.getAll()).thenReturn(List.of(new Book("Modernist novel",
                        new Author("James Joyce"), new Genre("Modernist novel")),
                new Book("Book", new Author("Author"), new Genre("Genre"))));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateByStatus() throws Exception {
        final Book book = new Book("Book",
                new Author("Author"), new Genre("Genre"));

        doNothing().when(bookService).updateBook
                ("Ulysses", "Book", "Author", "Genre");

        BookRequest bookRequest = new BookRequest();
        bookRequest.setTitle(book.getTitle());
        bookRequest.setAuthorName(book.getAuthor().getName());
        bookRequest.setGenreName(book.getGenre().getName());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(bookRequest);

        mockMvc.perform(put("/api/books").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteByStatus() throws Exception {
        final Book book = new Book("Book",
                new Author("Author"), new Genre("Genre"));

        doNothing().when(bookService).deleteBook("Ulysses");

        BookRequest bookRequest = new BookRequest();
        bookRequest.setTitle(book.getTitle());
        bookRequest.setAuthorName(book.getAuthor().getName());
        bookRequest.setGenreName(book.getGenre().getName());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(bookRequest);

        mockMvc.perform(delete("/api/books").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk());
    }
}
