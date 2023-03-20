package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    Film film;
    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    FilmService filmService = new FilmService(inMemoryFilmStorage, inMemoryUserStorage);
    FilmController controller = new FilmController(inMemoryFilmStorage, filmService);

    @BeforeEach
    void beforeEach() {
        film = Film.builder()
                .id(1)
                .name("Титаник")
                .description("Фильм про лодку")
                .releaseDate(LocalDate.of(1998, Month.FEBRUARY, 20))
                .duration(196)
                .build();
    }

    @Test
    void findAllWithoutData() {
        assertEquals(0, controller.findAll().size());
    }

    @Test
    void findAll() {
        controller.create(film);
        assertTrue(controller.findAll().contains(film));
    }

    @Test
    void create() {
        controller.create(film);
        assertTrue(controller.findAll().contains(film));
    }

    @Test
    void createWithInvalidNameException() {
        film.setName(null);
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.create(film));
        assertEquals("Название не может быть пустым.", exception.getMessage());
    }

    @Test
    void createWithInvalidDescriptionException() {
        film.setDescription("                                                                                        " +
                "                                                                                                    " +
                "                                                                                                    ");
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.create(film));
        assertEquals("Максимальная длина описания — 200 символов.", exception.getMessage());
    }

    @Test
    void createWithInvalidReleaseDateException() {
        film.setReleaseDate(LocalDate.of(1895, 1, 1));
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.create(film));
        assertEquals("Дата релиза — не раньше 28.12.1895.", exception.getMessage());
    }

    @Test
    void createWithInvalidDurationException() {
        film.setDuration(0);
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.create(film));
        assertEquals("Продолжительность фильма должна быть положительной.", exception.getMessage());
    }

    @Test
    void update() {
        Film film1 = Film.builder()
                .id(1)
                .name("Титаник")
                .description("Фильм про лодку от крутого режисёра")
                .releaseDate(LocalDate.of(1998, Month.FEBRUARY, 20))
                .duration(196)
                .build();

        controller.create(film);
        controller.updateOrCreate(film1);
        assertTrue(controller.findAll().contains(film1));
    }

    @Test
    void updateWithInvalidNameException() {
        Film film1 = Film.builder()
                .id(1)
                .name(null)
                .description("Фильм про лодку от крутого режисёра")
                .releaseDate(LocalDate.of(1998, Month.FEBRUARY, 20))
                .duration(196)
                .build();

        controller.create(film);
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.updateOrCreate(film1));
        assertEquals("Название не может быть пустым.", exception.getMessage());
    }

    @Test
    void updateWithInvalidDescriptionException() {
        Film film1 = Film.builder()
                .id(1)
                .name("Титаник")
                .description("                                                                                       " +
                        "                                                                                            " +
                        "                                                                                             ")
                .releaseDate(LocalDate.of(1998, Month.FEBRUARY, 20))
                .duration(196)
                .build();

        controller.create(film);
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.updateOrCreate(film1));
        assertEquals("Максимальная длина описания — 200 символов.", exception.getMessage());
    }

    @Test
    void updateWithInvalidReleaseDateException() {
        Film film1 = Film.builder()
                .id(1)
                .name("Титаник")
                .description("Фильм про лодку")
                .releaseDate(LocalDate.of(1895, 1, 1))
                .duration(196)
                .build();

        controller.create(film);
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.updateOrCreate(film1));
        assertEquals("Дата релиза — не раньше 28.12.1895.", exception.getMessage());
    }

    @Test
    void updateWithInvalidDurationException() {
        Film film1 = Film.builder()
                .id(1)
                .name("Титаник")
                .description("Фильм про лодку")
                .releaseDate(LocalDate.of(1998, Month.FEBRUARY, 20))
                .duration(0)
                .build();

        controller.create(film);
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.updateOrCreate(film1));
        assertEquals("Продолжительность фильма должна быть положительной.", exception.getMessage());
    }
}