package ru.otus.webbooklibrary.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.webbooklibrary.domain.Book;
import ru.otus.webbooklibrary.service.AuthorService;
import ru.otus.webbooklibrary.service.BookService;
import ru.otus.webbooklibrary.service.GenreService;

import java.util.List;

@Controller
public class BookPageController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    public BookPageController(BookService bookService, AuthorService authorService, GenreService genreService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @GetMapping("/books/add")
    public String savePage(Model model) {
        model.addAttribute("authors", authorService.getAll());
        model.addAttribute("genres", genreService.getAll());
        return "bookSave";
    }

    @GetMapping("/books/id")
    public String getBookById(@RequestParam String id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        return "bookById";
    }

    @GetMapping("/books/author/{author}")
    public String getBookByAuthor(@PathVariable String author, Model model) {
        model.addAttribute("book", bookService.getBookByAuthor(author));
        return "booksByAuthor";
    }

    @GetMapping("/books/genre/{genre}")
    public String getBookByGenre(@PathVariable String genre, Model model) {
        model.addAttribute("book", bookService.getBookByGenre(genre));
        return "booksByGenre";
    }

    @GetMapping("/books/comment")
    public String getBookByComment(@RequestParam("comment") String comment, Model model) {
        model.addAttribute("book", bookService.getBookByComment(comment));
        return "bookByComment";
    }

    @GetMapping("/books")
    public String getAll(Model model) {
        final List<Book> books = bookService.getAll();
        model.addAttribute("books", books);
        return "bookList";
    }

    @GetMapping("/books/edit")
    public String editPage(@RequestParam String id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        model.addAttribute("authors", authorService.getAll());
        model.addAttribute("genres", genreService.getAll());
        return "bookEdit";
    }
}
