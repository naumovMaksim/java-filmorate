package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.debug("Получен /GET запрос на получение списка всех фильмов.");
        log.debug("Ответ отправлен: {}", inMemoryFilmStorage.findAll());
        return inMemoryFilmStorage.findAll();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.debug("Получен /POST запрос на добавление нового фильма : {}", film);
        inMemoryFilmStorage.create(film);
        log.debug("Фильм добавлен: {}", inMemoryFilmStorage.findAll().contains(film));
        return film;
    }

    @PutMapping
    public Film updateOrCreate(@RequestBody Film film) {
        log.debug("Получен /PUT запрос на изменение данных фильма: {}", film);
        inMemoryFilmStorage.update(film);
        log.debug("Данные фильма изменены: {}", inMemoryFilmStorage.findAll().contains(film));
        return film;
    }
}
