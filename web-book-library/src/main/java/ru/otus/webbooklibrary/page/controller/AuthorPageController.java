package ru.otus.webbooklibrary.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.webbooklibrary.service.AuthorService;

@Controller
public class AuthorPageController {
    private final AuthorService authorService;

    public AuthorPageController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/authors/add")
    public String createPage() {
        return "authorSave";
    }

    @GetMapping("/authors/id")
    public String getAuthorById(@RequestParam String id, Model model) {
        model.addAttribute("author", authorService.getAuthorById(id));
        return "author";
    }

    @GetMapping("/authors")
    public String getAll() {
        return "authorList";
    }

    @GetMapping("/authors/edit")
    public String editPage(@RequestParam String id, Model model) {
        model.addAttribute("author", authorService.getAuthorById(id));
        return "authorEdit";
    }
}
