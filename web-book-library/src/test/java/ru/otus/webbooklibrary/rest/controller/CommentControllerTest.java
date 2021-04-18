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
import ru.otus.webbooklibrary.domain.Comment;
import ru.otus.webbooklibrary.rest.dto.CommentRequest;
import ru.otus.webbooklibrary.service.CommentServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentServiceImpl commentService;

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Test
    void testSaveByStatus() throws Exception {
        final CommentRequest commentRequest = new CommentRequest();
        commentRequest.setBook("Book");
        commentRequest.setContent("Comment");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(commentRequest);

        mockMvc.perform(post("/api/comments").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetCommentByContentByStatus() throws Exception {
        when(commentService.getCommentByContent("Comment")).thenReturn
                (List.of((new Comment("Comment", "Book"))));

        mockMvc.perform(get("/api/comments/Comment"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetCommentByBookTitleByStatus() throws Exception {
        when(commentService.getCommentsByBook("Book")).thenReturn(List.of
                (new Comment("Published in 1922", "Book"),
                        new Comment("Comment", "Book")));

        mockMvc.perform(get("/api/comments/book/Book"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllByStatus() throws Exception {
        when(commentService.getAll()).thenReturn(List.of
                (new Comment("Published in 1922", "Ulysses"),
                        new Comment("Comment", "Book")));
        mockMvc.perform(get("/api/comments"))
                .andExpect(status().isOk());

    }

    @Test
    void testEditByStatus() throws Exception {
        when(commentService.updateComment("Comment", "Published in 1922"))
                .thenReturn("Comment was updated");

        final CommentRequest commentRequest = new CommentRequest();
        commentRequest.setContent("Comment");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(commentRequest);

        mockMvc.perform(put("/api/comments").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteByContentByStatus() throws Exception {
        when(commentService.deleteComment("Comment")).thenReturn("Comment was deleted");

        final CommentRequest commentRequest = new CommentRequest();
        commentRequest.setContent("Comment");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(commentRequest);

        mockMvc.perform(delete("/api/comments").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk());
    }
}
