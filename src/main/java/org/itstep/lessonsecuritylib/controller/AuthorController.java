package org.itstep.lessonsecuritylib.controller;

import lombok.RequiredArgsConstructor;
import org.itstep.lessonsecuritylib.command.AuthorCommand;
import org.itstep.lessonsecuritylib.command.PublisherCommand;
import org.itstep.lessonsecuritylib.model.Author;
import org.itstep.lessonsecuritylib.model.Publisher;
import org.itstep.lessonsecuritylib.repository.AuthorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorRepository repository;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("authors", repository.findAll());
        return "authors";
    }

    @PostMapping
    String create(AuthorCommand command) {
//        System.out.println("firstName = " + firstName);
//        System.out.println("lastName = " + lastName);
        Author author = Author.fromCommand(command);
        repository.save(author);
        return "redirect:/authors";
    }

    @GetMapping(("delete/{id}"))
    String delete(@PathVariable Integer id) {
        Optional<Author> optionalAuthor = repository.findById(id);
        optionalAuthor.ifPresent(author -> repository.deleteById(id));
        return "redirect:/authors";
    }

    @GetMapping(("edit/{id}"))
    String datails(@PathVariable Integer id, Model model) {
        Optional<Author> optionalAuthor = repository.findById(id);
        if (optionalAuthor.isPresent()){
            Author author = optionalAuthor.get();
            model.addAttribute("author", author);
        }
        return "author_edit";
    }
    @PostMapping(("edit/{id}"))
    String update(@PathVariable Integer id, AuthorCommand command) {
        Optional<Author> optionalAuthor = repository.findById(id);
        if (optionalAuthor.isPresent()){
            Author author = optionalAuthor.get();
            author.setFirstName(command.firstName());
            author.setLastName(command.lastName());
            repository.save(author);
        }
        return "redirect:/authors";
    }
}
