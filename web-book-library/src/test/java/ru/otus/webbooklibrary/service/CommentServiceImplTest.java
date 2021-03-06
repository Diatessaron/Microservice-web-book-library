package ru.otus.webbooklibrary.service;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.webbooklibrary.domain.Author;
import ru.otus.webbooklibrary.domain.Book;
import ru.otus.webbooklibrary.domain.Comment;
import ru.otus.webbooklibrary.domain.Genre;
import ru.otus.webbooklibrary.repository.BookRepository;
import ru.otus.webbooklibrary.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DataMongoTest
@Import(CommentServiceImpl.class)
class CommentServiceImplTest {
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private CommentServiceImpl commentService;

    private final Book ulysses = new Book("Ulysses", new Author("James Joyce"),
            new Genre("Modernist novel"));
    private final Comment ulyssesComment = new Comment("Published in 1922", ulysses.getTitle());

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testSaveByComparing() {
        final Author foucault = new Author("Michel Foucault");
        final Genre philosophy = new Genre("Philosophy");
        final Book book = new Book("Discipline and Punish", foucault, philosophy);
        final Comment expected = new Comment("Published in 1975", book.getTitle());

        when(bookRepository.save(book)).thenReturn(book);
        when(bookRepository.findByTitle(book.getTitle())).thenReturn(List.of(book));
        when(commentRepository.save(expected)).thenReturn(expected);
        when(commentRepository.save(expected)).thenReturn(expected);
        when(commentRepository.findByContent(expected.getContent())).thenReturn(List.of(expected));

        bookRepository.save(book);
        commentService.saveComment(expected.getBook().getTitle(), expected.getContent());
        final Comment actual = commentService.getCommentByContent(expected.getContent()).get(0);

        assertEquals(expected.getContent(), actual.getContent());

        final InOrder inOrder = inOrder(bookRepository, commentRepository);
        inOrder.verify(bookRepository).save(book);
        inOrder.verify(commentRepository).save(expected);
        inOrder.verify(commentRepository).findByContent(expected.getContent());
    }

    @Test
    void shouldReturnCorrectCommentByContent() {
        when(commentRepository.findByContent(ulyssesComment.getContent())).thenReturn
                (List.of(ulyssesComment));

        final Comment actual = commentService.getCommentByContent(ulyssesComment.getContent()).get(0);

        assertEquals(ulyssesComment, actual);

        verify(commentRepository, times(1)).findByContent(ulyssesComment.getContent());
    }

    @Test
    void testGetCommentByBookMethod() {
        when(bookRepository.findByTitle(ulysses.getTitle())).thenReturn(List.of(ulysses));
        when(commentRepository.findByBook_Title(ulysses.getTitle())).thenReturn(List.of(ulyssesComment));

        final List<Comment> expected = List.of(ulyssesComment);
        final List<Comment> actual = commentService.getCommentsByBook("Ulysses");

        assertEquals(expected, actual);

        final InOrder inOrder = inOrder(bookRepository, commentRepository);
        inOrder.verify(commentRepository).findByBook_Title(ulysses.getTitle());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldReturnCorrectListOfComments() {
        final Author foucault = new Author("Michel Foucault");
        final Genre philosophy = new Genre("Philosophy");
        final Book book = new Book("Discipline and Punish", foucault, philosophy);

        final Comment disciplineAndPunishComment = new Comment("Published in 1975", book.getTitle());
        final List<Comment> expected = List.of(this.ulyssesComment, disciplineAndPunishComment);

        when(commentRepository.save(disciplineAndPunishComment)).thenReturn(disciplineAndPunishComment);
        when(commentRepository.findAll()).thenReturn(List.of(ulyssesComment, disciplineAndPunishComment));
        when(bookRepository.findByTitle(book.getTitle())).thenReturn(List.of(book));

        commentService.saveComment(disciplineAndPunishComment.getBook().getTitle(),
                disciplineAndPunishComment.getContent());
        final List<Comment> actual = commentService.getAll();

        assertEquals(expected, actual);

        final InOrder inOrder = inOrder(bookRepository, commentRepository);
        inOrder.verify(commentRepository).save(disciplineAndPunishComment);
        inOrder.verify(commentRepository).findAll();
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldUpdateCommentCorrectly() {
        final Comment comment = new Comment("Comment", ulysses.getTitle());

        when(commentRepository.findById(ulyssesComment.getContent())).thenReturn
                (Optional.of(ulyssesComment));
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentRepository.findByContent(comment.getContent())).thenReturn(List.of(comment));

        commentService.updateComment("Published in 1922", "Comment");

        final Comment actualComment = commentService.getCommentByContent(comment.getContent()).get(0);
        assertThat(actualComment).isNotNull().matches(s -> !s.getContent().isBlank())
                .matches(s -> s.getContent().equals("Comment"));

        final InOrder inOrder = inOrder(bookRepository, commentRepository);
        inOrder.verify(commentRepository).findById("Published in 1922");
        inOrder.verify(commentRepository).save(comment);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void testDeleteByIdMethodByResultStringComparing() {
        when(commentRepository.findById(ulyssesComment.getContent())).thenReturn
                (Optional.of(ulyssesComment));
        doNothing().when(commentRepository).deleteByContent(ulyssesComment.getContent());

        final String expected = "Ulysses comment was deleted";
        final String actual = commentService.deleteComment("Published in 1922");

        assertEquals(expected, actual);

        final InOrder inOrder = inOrder(bookRepository, commentRepository);
        inOrder.verify(commentRepository).findById(ulyssesComment.getContent());
        inOrder.verify(commentRepository).deleteById(ulyssesComment.getContent());
    }
}
