package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genere;
import ru.yandex.practicum.filmorate.service.GenereService;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
@Slf4j
public class GenereController {

    private final GenereService genereService;

    @GetMapping
    public Collection<Genere> getAllGeners() {
        log.debug("Пришел GET запрос на получение всего списка жанров");
        Collection<Genere> geners = genereService.getAllGeneres();
        log.debug("Ответ отправлен: {}", geners);
        return geners;
    }

    @GetMapping("/{id}")
    public Genere getGenereById(@PathVariable int id) {
        log.debug("Пришел GET запрос на получение жанра по id = {}", id);
        Genere genere = genereService.getGenereById(id);
        log.debug("Ответ отправлен: {}", genere);
        return genere;
    }
}
