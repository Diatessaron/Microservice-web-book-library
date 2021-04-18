package ru.otus.webbooklibrary.page.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.webbooklibrary.domain.Author;
import ru.otus.webbooklibrary.domain.Book;
import ru.otus.webbooklibrary.domain.Genre;
import ru.otus.webbooklibrary.service.AuthorServiceImpl;
import ru.otus.webbooklibrary.service.BookServiceImpl;
import ru.otus.webbooklibrary.service.GenreServiceImpl;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookPageController.class)
class BookPageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookServiceImpl bookService;
    @MockBean
    private AuthorServiceImpl authorService;
    @MockBean
    private GenreServiceImpl genreService;

    @Test
    void testGetBookByIdByStatus() throws Exception {
        when(bookService.getBookById("Book")).thenReturn(new Book("Book", new Author("Author"),
                new Genre("Genre")));

        mockMvc.perform(get("/books/id")
                .param("id", "Book"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetBookByAuthorByStatus() throws Exception {
        when(bookService.getBookByAuthor("Author")).thenReturn(List.of(new Book("Book", new Author("Author"),
                new Genre("Genre"))));

        mockMvc.perform(get("/books/author/Author"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetBookByGenreByStatus() throws Exception {
        when(bookService.getBookByGenre("Genre")).thenReturn(List.of(new Book("Book", new Author("Author"),
                new Genre("Genre"))));

        mockMvc.perform(get("/books/genre/Genre"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetBookByCommentByStatus() throws Exception {
        when(bookService.getBookByComment("Comment")).thenReturn(new Book("Book", new Author("Author"),
                new Genre("Genre")));

        mockMvc.perform(get("/books/comment")
                .param("comment", "Comment"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllByStatusByStatus() throws Exception {
        when(bookService.getAll()).thenReturn(List.of
                (new Book("Modernist novel", new Author("James Joyce"), new Genre("Modernist novel")),
                        new Book("Book", new Author("Author"), new Genre("Genre"))));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk());
    }
}
