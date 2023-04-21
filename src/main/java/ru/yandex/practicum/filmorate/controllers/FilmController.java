package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{filmId}")
    public Film findFilm(@PathVariable int filmId) {
        log.debug("Пришел запрос на получение фильма по id: {}", filmId);
        Film film = filmService.findFilm(filmId);
        log.debug("Ответ отправлен: {}", film);
        return film;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.debug("Получен /GET запрос на получение списка всех фильмов.");
        Collection<Film> films = filmService.findAll();
        log.debug("Ответ отправлен: {}", films);
        return films;
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.debug("Получен /POST запрос на добавление нового фильма : {}", film);
        Film film1 = filmService.create(film);
        log.debug("Фильм добавлен: {}", film1);
        return film1;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.debug("Получен /PUT запрос на изменение данных фильма: {}", film);
        filmService.updateFilm(film);
        Film updatedFilm = filmService.findFilm(film.getId());
        log.debug("Данные фильма изменены: {}", updatedFilm);
        return updatedFilm;
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable int filmId, @PathVariable int userId) {
        log.debug("Пришел /PUT запрос на лайк фильма с id {}", filmId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable int filmId, @PathVariable int userId) {
        log.debug("Пришел запрос на удаление лайка с фильма с id {}", filmId);
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> popularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        log.debug("Пришел /GET запрос на получение списка самых популярных фильмов в количестве {}", count);
        Collection<Film> films = filmService.popularFilms(count);
        log.debug("Ответ отправлен: {}", films);
        return films;
    }
}
//{
//        "name": "Титаник",
//        "description": "Фильм про корабль",
//        "releaseDate": "1999-12-12",
//        "duration": 60,
//        "rate": 2,
//        "mpa": {"id": 1},
//        "genres": [{"id": 1}]
//        }
