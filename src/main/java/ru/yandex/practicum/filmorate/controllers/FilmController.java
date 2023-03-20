package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    FilmStorage filmStorage;
    FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping("/{filmId}")
    public Film findFilm(@PathVariable int filmId) {
        log.debug("Пришел запрос на получение фильма по id: {}", filmId);
        log.debug("Ответ отправлен: {}", filmStorage.findFilm(filmId));
        return filmStorage.findFilm(filmId);
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.debug("Получен /GET запрос на получение списка всех фильмов.");
        log.debug("Ответ отправлен: {}", filmStorage.findAll());
        return filmStorage.findAll();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.debug("Получен /POST запрос на добавление нового фильма : {}", film);
        filmStorage.create(film);
        log.debug("Фильм добавлен: {}", filmStorage.findAll().contains(film));
        return film;
    }

    @PutMapping
    public Film updateOrCreate(@RequestBody Film film) {
        log.debug("Получен /PUT запрос на изменение данных фильма: {}", film);
        filmStorage.update(film);
        log.debug("Данные фильма изменены: {}", filmStorage.findAll().contains(film));
        return film;
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable int filmId, @PathVariable int userId) {
        log.debug("Пришел /PUT запрос на лайк фильма с id {}", filmId);
        filmService.addLike(filmId, userId);
        Film film = filmStorage.findFilm(filmId);
        log.debug("Ответ отправлен {}", film.getUsersLikes().contains(userId));
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable int filmId, @PathVariable int userId) {
        log.debug("Пришел запрос на удаление лайка с фильма с id {}", filmId);
        filmService.deleteLike(filmId, userId);
        Film film = filmStorage.findFilm(filmId);
        log.debug("Ответ отправлен {}", !film.getUsersLikes().contains(userId));
    }

    @GetMapping("/popular")
    public Collection<Film> popularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        log.debug("Пришел /GET запрос на получение списка самых популярных фильмов в количестве {}", count);
        log.debug("Ответ отправлен: {}", filmService.popularFilms(count));
        return filmService.popularFilms(count);
    }
}
//{
//        "name": "Титаник",
//        "description": "что то",
//        "releaseDate": "2001-12-25",
//        "duration": "5"
//        }
