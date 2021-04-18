package ru.otus.webbooklibrary.page.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.webbooklibrary.domain.Author;
import ru.otus.webbooklibrary.service.AuthorServiceImpl;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorPageController.class)
class AuthorPageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorServiceImpl authorService;

    @Test
    void testGetAuthorByIdByCorrectStatus() throws Exception {
        when(authorService.getAuthorById("Author")).thenReturn(new Author("Author"));

        mockMvc.perform(MockMvcRequestBuilders.get("/authors/id")
                .param("id", "Author"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllByCorrectStatus() throws Exception {
        when(authorService.getAll()).thenReturn(List.of(new Author("James Joyce"), new Author("Author")));

        mockMvc.perform(MockMvcRequestBuilders.get("/authors"))
                .andExpect(status().isOk());
    }
}
