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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.webbooklibrary.domain.Author;
import ru.otus.webbooklibrary.rest.dto.AuthorRequest;
import ru.otus.webbooklibrary.service.AuthorServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorServiceImpl authorService;

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Test
    void testCreateByStatus() throws Exception {
        AuthorRequest authorRequest = new AuthorRequest();
        authorRequest.setAuthor("Author");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(authorRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/authors").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetAuthorByIdByStatus() throws Exception {
        when(authorService.getAuthorById("id")).thenReturn(new Author("Author"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/authors/id")
                .param("id", "id"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAuthorByNameByStatus() throws Exception {
        when(authorService.getAuthorByName("Author")).thenReturn(List.of(new Author("Author")));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/authors/Author"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllByStatus() throws Exception {
        when(authorService.getAll()).thenReturn(List.of(new Author("James Joyce"),
                new Author("Author")));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/authors"))
                .andExpect(status().isOk());
    }

    @Test
    void testEditByStatus() throws Exception {
        when(authorService.updateAuthor("id", "Author"))
                .thenReturn("Author was updated");

        AuthorRequest authorRequest = new AuthorRequest();
        authorRequest.setId("id");
        authorRequest.setAuthor("Author");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(authorRequest);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/authors").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteByNameByStatus() throws Exception {
        when(authorService.deleteAuthor("Author")).thenReturn("Author was deleted");

        AuthorRequest authorRequest = new AuthorRequest();
        authorRequest.setId("Author");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(authorRequest);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/authors").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk());
    }
}
