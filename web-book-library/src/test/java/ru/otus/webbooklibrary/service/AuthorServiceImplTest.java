package ru.otus.webbooklibrary.service;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.webbooklibrary.domain.Author;
import ru.otus.webbooklibrary.repository.AuthorRepository;
import ru.otus.webbooklibrary.repository.BookRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthorServiceImplTest {
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private AuthorServiceImpl service;

    private final Author jamesJoyce = new Author("James Joyce");

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void testSaveByComparing() {
        final Author foucault = new Author("Michel Foucault");

        when(authorRepository.save(foucault)).thenReturn(foucault);
        when(authorRepository.findByName(foucault.getName())).thenReturn(List.of(foucault));

        service.saveAuthor(foucault.getName());

        final Author actual = service.getAuthorByName("Michel Foucault").get(0);

        assertNotNull(actual);
        assertEquals(foucault.getName(), actual.getName());

        verify(authorRepository, times(1)).save(foucault);
    }

    @Test
    void shouldReturnCorrectAuthorByName() {
        when(authorRepository.findByName(jamesJoyce.getName())).thenReturn(List.of(jamesJoyce));

        final Author actual = service.getAuthorByName(jamesJoyce.getName()).get(0);

        assertEquals(jamesJoyce, actual);

        verify(authorRepository, times(1)).findByName(jamesJoyce.getName());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testUpdateAuthorMethodByComparing() {
        final Author author = new Author("Author");

        when(authorRepository.findById(jamesJoyce.getName())).thenReturn(Optional.of(jamesJoyce));
        when(authorRepository.findByName(author.getName())).thenReturn(List.of(author));
        when(authorRepository.save(author)).thenReturn(author);
        when(bookRepository.findByAuthor_Name(author.getName())).thenReturn(List.of());

        service.updateAuthor(jamesJoyce.getName(), "Author");

        final Author actualAuthor = service.getAuthorByName("Author").get(0);

        assertThat(actualAuthor).isNotNull().matches(s -> !s.getName().isBlank())
                .matches(s -> s.getName().equals("Author"));

        final InOrder inOrder = inOrder(authorRepository, bookRepository);
        inOrder.verify(authorRepository).findById("James Joyce");
        inOrder.verify(authorRepository).save(author);
        inOrder.verify(bookRepository).findByAuthor_Name("James Joyce");
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void authorShouldBeDeletedCorrectly() {
        when(authorRepository.findById(jamesJoyce.getName())).thenReturn(Optional.of(jamesJoyce));
        doNothing().when(authorRepository).deleteById(jamesJoyce.getName());
        doNothing().when(bookRepository).deleteByAuthor_Name(jamesJoyce.getName());

        final String expected = "James Joyce was deleted";
        final String actual = service.deleteAuthor("James Joyce");

        assertEquals(expected, actual);

        final InOrder inOrder = inOrder(authorRepository, bookRepository);
        inOrder.verify(authorRepository).findById(jamesJoyce.getName());
        inOrder.verify(authorRepository).deleteById(jamesJoyce.getName());
        inOrder.verify(bookRepository).deleteByAuthor_Name(jamesJoyce.getName());
    }
}
