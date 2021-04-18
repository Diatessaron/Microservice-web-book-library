package ru.otus.webbooklibrary.page.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.webbooklibrary.domain.Comment;
import ru.otus.webbooklibrary.service.BookServiceImpl;
import ru.otus.webbooklibrary.service.CommentServiceImpl;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentPageController.class)
class CommentPageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentServiceImpl commentService;
    @MockBean
    private BookServiceImpl bookService;

    @Test
    void testGetCommentByIdByStatus() throws Exception {
        when(commentService.getCommentById("Comment")).thenReturn(new Comment("Comment", "Book"));

        mockMvc.perform(get("/comments/id")
                .param("id", "Comment"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetCommentByBookTitleByStatus() throws Exception {
        when(commentService.getCommentsByBook("Book")).thenReturn(List.of
                (new Comment("Published in 1922", "Book"),
                        new Comment("Comment", "Book")));

        mockMvc.perform(get("/comments/book/Book"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllByStatus() throws Exception {
        when(commentService.getAll()).thenReturn(List.of
                (new Comment("Published in 1922", "Ulysses"),
                        new Comment("Comment", "Book")));

        mockMvc.perform(get("/comments"))
                .andExpect(status().isOk());
    }
}
