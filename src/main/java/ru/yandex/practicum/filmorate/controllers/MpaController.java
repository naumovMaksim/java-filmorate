package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@Slf4j
@RequiredArgsConstructor
public class MpaController {

    private final MpaService mpaService;

    @GetMapping
    public Collection<Mpa> getAllRatings() {
        log.debug("Поступил GET запрос на получение всех рейтингов");
        Collection<Mpa> ratings = mpaService.getAllRatings();
        log.debug("Ответ отправлен: {}", ratings);
        return ratings;
    }

    @GetMapping("/{id}")
    public Mpa getRatingById(@PathVariable int id) {
        log.debug("Поступил GET запрос на получение рейтинга по id");
        Mpa rating = mpaService.getRatingById(id);
        log.debug("Ответ отправлен: {}", rating);
        return rating;
    }
}
