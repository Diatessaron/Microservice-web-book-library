package ru.otus.webbooklibrary.page.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.webbooklibrary.domain.Genre;
import ru.otus.webbooklibrary.service.GenreServiceImpl;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenrePageController.class)
class GenrePageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreServiceImpl genreService;

    @Test
    void testGetGenreByNameByStatus() throws Exception {
        when(genreService.getGenreById("Genre")).thenReturn(new Genre("Genre"));

        mockMvc.perform(MockMvcRequestBuilders.get("/genres/id")
                .param("id", "Genre"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllByStatus() throws Exception {
        when(genreService.getAll()).thenReturn(List.of(new Genre("Modernist novel"), new Genre("Genre")));

        mockMvc.perform(MockMvcRequestBuilders.get("/genres"))
                .andExpect(status().isOk());
    }
}
