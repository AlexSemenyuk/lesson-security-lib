package org.itstep.lessonsecuritylib.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.itstep.lessonsecuritylib.command.BookCommand;
import org.itstep.lessonsecuritylib.model.Author;
import org.itstep.lessonsecuritylib.model.Book;
import org.itstep.lessonsecuritylib.model.Publisher;
import org.itstep.lessonsecuritylib.repository.AuthorRepository;
import org.itstep.lessonsecuritylib.repository.BookRepository;
import org.itstep.lessonsecuritylib.repository.PublisherRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("publishers", publisherRepository.findAll());
        model.addAttribute("authors", authorRepository.findAll());
        return "books";
    }

    @PostMapping
    public String create(BookCommand command) {
        log.info("BookCommand {}", command);
        Optional<Publisher> optionalPublisher = publisherRepository.findById(command.publisherId());
        optionalPublisher.ifPresent(publisher -> {
            Book book = Book.fromCommand(command);
            List<Author> authors = authorRepository.findAllById(command.authorsIds());
            book.setPublisher(publisher);
            authors.forEach(book::addAuthor);
            bookRepository.save(book);
        });
        return "redirect:/books";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable Integer id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        optionalBook.ifPresent(book -> bookRepository.deleteById(id));
        return "redirect:/books";
    }
}



