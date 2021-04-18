package ru.otus.webbooklibrary.service;

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

    @Transactional
    @Override
    public String saveComment(String bookTitle, String commentContent) {
        final Comment comment = new Comment(commentContent, bookTitle);

        commentRepository.save(comment);

        return "You successfully added a comment to " + bookTitle;
    }

    @Transactional(readOnly = true)
    @Override
    public Comment getCommentById(String id){
        return commentRepository.findById(id).orElseThrow
                (() -> new IllegalArgumentException("Incorrect comment id"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getCommentByContent(String content) {
        return commentRepository.findByContent(content);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getCommentsByBook(String bookTitle) {
        return commentRepository.findByBook_Title(bookTitle);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

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

    @Transactional
    @Override
    public String deleteComment(String id) {
        final Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect comment id"));

        commentRepository.deleteById(id);

        return comment.getBook().getTitle() + " comment was deleted";
    }
}
