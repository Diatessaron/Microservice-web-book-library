package ru.otus.webbooklibrary.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.webbooklibrary.domain.Comment;
import ru.otus.webbooklibrary.repository.BookRepository;
import ru.otus.webbooklibrary.repository.CommentRepository;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    public CommentServiceImpl(CommentRepository commentRepository, BookRepository bookRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
    }

    @HystrixCommand(defaultFallback = "getEmptyStringResult")
    @Transactional
    @Override
    public String saveComment(String bookTitle, String commentContent) {
        final Comment comment = new Comment(commentContent, bookTitle);

        commentRepository.save(comment);

        return "You successfully added a comment to " + bookTitle;
    }

    @HystrixCommand(defaultFallback = "getEmptyCommentResult")
    @Transactional(readOnly = true)
    @Override
    public Comment getCommentById(String id){
        return commentRepository.findById(id).orElseThrow
                (() -> new IllegalArgumentException("Incorrect comment id"));
    }

    @HystrixCommand(defaultFallback = "getEmptyCommentListResult")
    @Transactional(readOnly = true)
    @Override
    public List<Comment> getCommentByContent(String content) {
        return commentRepository.findByContent(content);
    }

    @HystrixCommand(defaultFallback = "getEmptyCommentListResult")
    @Transactional(readOnly = true)
    @Override
    public List<Comment> getCommentsByBook(String bookTitle) {
        return commentRepository.findByBook_Title(bookTitle);
    }

    @HystrixCommand(defaultFallback = "getEmptyCommentListResult")
    @Transactional(readOnly = true)
    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @HystrixCommand(defaultFallback = "getEmptyStringResult")
    @Transactional
    @Override
    public String updateComment(String id, String commentContent) {
        final Comment comment = commentRepository.findById(id).orElseThrow
                (() -> new IllegalArgumentException("Incorrect comment id"));

        comment.setContent(commentContent);
        comment.setBook(comment.getBook().getTitle());

        commentRepository.save(comment);

        return comment.getBook().getTitle() + " comment was updated";
    }

    @HystrixCommand(defaultFallback = "getEmptyStringResult")
    @Transactional
    @Override
    public String deleteComment(String id) {
        final Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect comment id"));

        commentRepository.deleteById(id);

        return comment.getBook().getTitle() + " comment was deleted";
    }

    public String getEmptyStringResult() {
        return "Operation can not be executed.";
    }

    public Comment getEmptyCommentResult() {
        Comment comment = new Comment();
        comment.setId("N/A");
        comment.setContent("N/A");
        comment.setBook("N/A");

        return comment;
    }

    public List<Comment> getEmptyCommentListResult() {
        return List.of();
    }
}
