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

import java.util.ArrayList;
import java.util.HashSet;
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

    @GetMapping(("/edit/{id}"))
    String datails(@PathVariable Integer id, Model model) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()){
            Book book = optionalBook.get();
            model.addAttribute("book", book);
            model.addAttribute("bookAuthors", book.getAuthors());
            model.addAttribute("publishers", publisherRepository.findAll());
            model.addAttribute("authors", authorRepository.findAll());
        }
        return "book_edit";
    }
    @PostMapping(("edit/{id}"))
    String update(@PathVariable Integer id, BookCommand command) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        Optional<Publisher> optionalPublisher = publisherRepository.findById(command.publisherId());
        Optional<List<Author>> optionalAuthors = Optional.of(authorRepository.findAllById(command.authorsIds()));
        if (optionalBook.isPresent() &&
                optionalPublisher.isPresent() &&
                optionalAuthors.isPresent()){
            Book book = optionalBook.get();
            book.setTitle(command.title());

            Publisher publisher = optionalPublisher.get();
            book.setPublisher(publisher);

            List<Author> authors = optionalAuthors.get();
            book.setAuthors(new ArrayList<>());
            authors.stream().forEach(author -> book.getAuthors().add(author));
            bookRepository.save(book);
        }
        return "redirect:/books";
    }
}



