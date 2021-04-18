package ru.otus.webbooklibrary.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.webbooklibrary.domain.Genre;
import ru.otus.webbooklibrary.rest.dto.GenreRequest;
import ru.otus.webbooklibrary.service.GenreService;

import java.util.List;

@RestController
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @PostMapping(value = "/api/genres",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> create(@Validated @RequestBody GenreRequest genreRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(genreService.saveGenre
                (genreRequest.getGenre()));
    }

    @GetMapping("/api/genres/id")
    public ResponseEntity<Genre> getGenreById(@RequestParam String id){
        return ResponseEntity.status(HttpStatus.OK).body(genreService.getGenreById(id));
    }

    @GetMapping("/api/genres/{genre}")
    public ResponseEntity<Genre> getGenreByName(@PathVariable String genre) {
        return ResponseEntity.status(HttpStatus.OK).body(genreService.getGenreByName(genre));
    }

    @GetMapping("/api/genres")
    public ResponseEntity<List<Genre>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(genreService.getAll());
    }

    @PutMapping(value = "/api/genres",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> edit(@Validated  @RequestBody GenreRequest genreRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(genreService.updateGenre
                (genreRequest.getId(), genreRequest.getGenre()));
    }

    @DeleteMapping(value = "/api/genres",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteByName(@Validated @RequestBody GenreRequest genreRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(genreService.deleteGenre(genreRequest.getId()));
    }
}
