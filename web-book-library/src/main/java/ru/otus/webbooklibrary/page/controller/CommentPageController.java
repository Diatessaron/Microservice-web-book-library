package ru.otus.webbooklibrary.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.webbooklibrary.domain.Comment;
import ru.otus.webbooklibrary.service.BookService;
import ru.otus.webbooklibrary.service.CommentService;

import java.util.List;

@Controller
public class CommentPageController {
    private final CommentService commentService;
    private final BookService bookService;

    public CommentPageController(CommentService commentService, BookService bookService) {
        this.bookService = bookService;
        this.commentService = commentService;
    }

    @GetMapping("/comments/add")
    public String savePage(Model model) {
        model.addAttribute("books", bookService.getAll());
        return "commentSave";
    }

    @GetMapping("/comments/id")
    public String getCommentById(@RequestParam String id, Model model) {
        model.addAttribute("comment", commentService.getCommentById(id));
        return "comment";
    }

    @GetMapping("/comments/book/{bookTitle}")
    public String getCommentByBookTitle(@PathVariable String bookTitle, Model model) {
        model.addAttribute("comments", commentService.getCommentsByBook(bookTitle));
        return "commentListByBook";
    }

    @GetMapping("/comments")
    public String getAll(Model model) {
        final List<Comment> comments = commentService.getAll();
        model.addAttribute("comments", comments);
        return "commentList";
    }

    @GetMapping("/comments/edit")
    public String editPage(@RequestParam String id, Model model) {
        model.addAttribute("comment", commentService.getCommentById(id));
        return "commentEdit";
    }
}
