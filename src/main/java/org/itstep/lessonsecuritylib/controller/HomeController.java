package org.itstep.lessonsecuritylib.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.itstep.lesson091.repository.AuthorRepository;
//import org.itstep.lesson091.repository.BookRepository;
//import org.itstep.lesson091.repository.PublisherRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
//    private final BookRepository bookRepository;
//    private final PublisherRepository publisherRepository;
//    private final AuthorRepository authorRepository;

    @GetMapping("/")
    public String home() {
        return "home";
    }

}
