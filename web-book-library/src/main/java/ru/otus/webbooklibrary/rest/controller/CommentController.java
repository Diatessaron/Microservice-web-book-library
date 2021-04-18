package ru.otus.webbooklibrary.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.webbooklibrary.domain.Comment;
import ru.otus.webbooklibrary.rest.dto.CommentRequest;
import ru.otus.webbooklibrary.service.CommentService;

import java.util.List;

@RestController
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping(value = "/api/comments",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> save(@Validated @RequestBody CommentRequest commentRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.saveComment
                (commentRequest.getBook(), commentRequest.getContent()));
    }

    @GetMapping("/api/comments/id")
    public ResponseEntity<Comment> getCommentById(@RequestParam String id){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentById(id));
    }

    @GetMapping("/api/comments/{comment}")
    public ResponseEntity<List<Comment>> getCommentByContent(@PathVariable String comment) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentByContent(comment));
    }

    @GetMapping("/api/comments/book/{bookTitle}")
    public ResponseEntity<List<Comment>> getCommentByBookTitle(@PathVariable String bookTitle) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentsByBook(bookTitle));
    }

    @GetMapping("/api/comments")
    public ResponseEntity<List<Comment>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAll());
    }

    @PutMapping(value = "/api/comments",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> edit(@Validated @RequestBody CommentRequest commentRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment
                (commentRequest.getId(), commentRequest.getContent()));
    }

    @DeleteMapping(value = "/api/comments",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteByContent(@Validated @RequestBody CommentRequest commentRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.deleteComment(commentRequest.getId()));
    }
}
