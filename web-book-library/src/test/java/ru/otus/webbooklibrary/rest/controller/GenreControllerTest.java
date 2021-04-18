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
import ru.otus.webbooklibrary.domain.Genre;
import ru.otus.webbooklibrary.rest.dto.GenreRequest;
import ru.otus.webbooklibrary.service.GenreServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreController.class)
class GenreControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreServiceImpl genreService;

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Test
    void testCreateByStatus() throws Exception {
        GenreRequest genreRequest = new GenreRequest();
        genreRequest.setGenre("Modernist novel");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(genreRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/genres").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetGenreByIdByStatus() throws Exception {
        when(genreService.getGenreById("Id")).thenReturn(new Genre("Genre"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/genres/id")
                .param("id", "id"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetGenreByNameByStatus() throws Exception {
        when(genreService.getGenreByName("Genre")).thenReturn(new Genre("Genre"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/genres/Genre"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllByStatus() throws Exception {
        when(genreService.getAll()).thenReturn(List.of(new Genre("Modernist novel"),
                new Genre("Genre")));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/genres"))
                .andExpect(status().isOk());
    }

    @Test
    void testEditByStatus() throws Exception {
        when(genreService.updateGenre("id", "Genre"))
                .thenReturn("Genre was updated");

        GenreRequest genreRequest = new GenreRequest();
        genreRequest.setId("id");
        genreRequest.setGenre("Genre");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(genreRequest);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/genres").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteByNameByStatus() throws Exception {
        when(genreService.deleteGenre("Modernist novel")).thenReturn("Modernist novel was deleted");

        GenreRequest genreRequest = new GenreRequest();
        genreRequest.setId("id");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(genreRequest);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/genres").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().isOk());
    }
}
