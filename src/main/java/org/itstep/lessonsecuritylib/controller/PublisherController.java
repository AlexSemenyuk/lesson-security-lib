package org.itstep.lessonsecuritylib.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.itstep.lessonsecuritylib.command.PublisherCommand;
import org.itstep.lessonsecuritylib.model.Author;
import org.itstep.lessonsecuritylib.model.Publisher;
import org.itstep.lessonsecuritylib.repository.PublisherRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/publishers")
@RequiredArgsConstructor
public class PublisherController {
    private final PublisherRepository repository;

    @GetMapping
    String index(Model model) {
        model.addAttribute("publishers", repository.findAll());
        return "publishers";
    }

    @PostMapping
    String create(@ModelAttribute @Validated PublisherCommand command,
                  BindingResult bindingResult,
                  RedirectAttributes model) {
        Publisher publisher = Publisher.fromCommand(command);
        log.info(command.toString());
        log.info(bindingResult.toString());
        try {
            if (!bindingResult.hasErrors()) {
                repository.save(publisher);
                model.addFlashAttribute("message", "Publisher created successfully");
            } else {
                model.addFlashAttribute("error", "Error with fields: %s".formatted(
                        bindingResult.getFieldErrors()
                                .stream()
                                .map(fieldError -> fieldError.getField() + " because of " + fieldError.getDefaultMessage())
                                .distinct()
                                .collect(Collectors.joining(","))));
            }
        } catch (IllegalArgumentException | OptimisticEntityLockException ex) {
            model.addFlashAttribute("error", "Error creating publisher because of illegal argu-ment or optimistic entry lock");
        } catch (Exception ex) {
            model.addFlashAttribute("error", "Error creating publisher because of non unique publisher name");
        }
        return "redirect:/publishers";
    }

    @DeleteMapping(("delete/{id}"))
    String delete(@PathVariable Integer id) {
        Optional<Publisher> optionalPublisher = repository.findById(id);
        optionalPublisher.ifPresent(author -> repository.deleteById(id));
        return "redirect:/publishers";
    }
}



