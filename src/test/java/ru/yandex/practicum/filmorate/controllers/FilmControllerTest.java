package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    Film film;
    User user;
    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    FilmService filmService = new FilmService(inMemoryFilmStorage, inMemoryUserStorage);
    FilmController controller = new FilmController(filmService);

    @BeforeEach
    void beforeEach() {
        film = Film.builder()
                .id(1)
                .name("Титаник")
                .description("Фильм про лодку")
                .releaseDate(LocalDate.of(1998, Month.FEBRUARY, 20))
                .duration(196)
                .usersLikes(new HashSet<>())
                .build();
        user = User.builder()
                .id(1)
                .email("Santa@mail.ru")
                .login("Santa")
                .name("Санта")
                .birthday(LocalDate.of(0, Month.DECEMBER, 6))
                .friends(new HashSet<>())
                .build();
    }

    @Test
    void findFilm() {
        controller.create(film);
        assertEquals(film, controller.findFilm(film.getId()));
    }

    @Test
    void findFilmWithWrongData() {
        controller.create(film);
        assertEquals(film, controller.findFilm(film.getId()));
        final DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> controller.findFilm(0));
        assertEquals("Фильм не найден.", exception.getParameter());
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
        assertEquals("Название не может быть пустым.", exception.getParameter());
    }

    @Test
    void createWithInvalidDescriptionException() {
        film.setDescription("                                                                                        " +
                "                                                                                                    " +
                "                                                                                                    ");
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.create(film));
        assertEquals("Максимальная длина описания — 200 символов.", exception.getParameter());
    }

    @Test
    void createWithInvalidReleaseDateException() {
        film.setReleaseDate(LocalDate.of(1895, 1, 1));
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.create(film));
        assertEquals("Дата релиза — не раньше 28.12.1895.", exception.getParameter());
    }

    @Test
    void createWithInvalidDurationException() {
        film.setDuration(0);
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.create(film));
        assertEquals("Продолжительность фильма должна быть положительной.", exception.getParameter());
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
        assertEquals("Название не может быть пустым.", exception.getParameter());
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
        assertEquals("Максимальная длина описания — 200 символов.", exception.getParameter());
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
        assertEquals("Дата релиза — не раньше 28.12.1895.", exception.getParameter());
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
        assertEquals("Продолжительность фильма должна быть положительной.", exception.getParameter());
    }

    @Test
    void addLike() {
        controller.create(film);
        inMemoryUserStorage.create(user);
        filmService.addLike(film.getId(), user.getId());
        assertTrue(film.getUsersLikes().contains(user.getId()));
    }

    @Test
    void addLikeWithWrongFilmData() {
        controller.create(film);
        inMemoryUserStorage.create(user);
        final DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> filmService.addLike(0, user.getId()));
        assertEquals("Фильм не найден.", exception.getParameter());
    }

    @Test
    void addLikeWithWrongUserData() {
        controller.create(film);
        inMemoryUserStorage.create(user);
        final DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> filmService.addLike(film.getId(), 0));
        assertEquals("Пользователь не найден.", exception.getParameter());
    }

    @Test
    void deleteLike() {
        controller.create(film);
        inMemoryUserStorage.create(user);
        filmService.addLike(film.getId(), user.getId());
        assertTrue(film.getUsersLikes().contains(user.getId()));
        controller.deleteLike(film.getId(), user.getId());
        assertFalse(film.getUsersLikes().contains(user.getId()));
    }

    @Test
    void deleteLikeWithWrongFilmData() {
        controller.create(film);
        inMemoryUserStorage.create(user);
        filmService.addLike(film.getId(), user.getId());
        assertTrue(film.getUsersLikes().contains(user.getId()));
        final DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> controller.deleteLike(0, user.getId()));
        assertTrue(film.getUsersLikes().contains(user.getId()));
        assertEquals("Фильм не найден.", exception.getParameter());
    }

    @Test
    void deleteLikeWithWrongUserData() {
        controller.create(film);
        inMemoryUserStorage.create(user);
        filmService.addLike(film.getId(), user.getId());
        assertTrue(film.getUsersLikes().contains(user.getId()));
        final DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> controller.deleteLike(film.getId(), 0));
        assertTrue(film.getUsersLikes().contains(user.getId()));
        assertEquals("Пользователь не найден.", exception.getParameter());
    }

    @Test
    void popularFilms() {
        Film film2 = Film.builder()
                .name("Ти")
                .description("Фильм про лодку")
                .releaseDate(LocalDate.of(1998, Month.FEBRUARY, 20))
                .duration(196)
                .usersLikes(new HashSet<>())
                .build();
        Film film3 = Film.builder()
                .name("Титан")
                .description("Фильм про лодку")
                .releaseDate(LocalDate.of(1998, Month.FEBRUARY, 20))
                .duration(196)
                .usersLikes(new HashSet<>())
                .build();
        User user2 = User.builder()
                .id(1)
                .email("Santa@mail.ru")
                .login("Santa")
                .name("Санта")
                .birthday(LocalDate.of(0, Month.DECEMBER, 6))
                .friends(new HashSet<>())
                .build();
        User user3 = User.builder()
                .id(1)
                .email("Santa@mail.ru")
                .login("Santa")
                .name("Санта")
                .birthday(LocalDate.of(0, Month.DECEMBER, 6))
                .friends(new HashSet<>())
                .build();
        controller.create(film);
        controller.create(film2);
        controller.create(film3);
        inMemoryUserStorage.create(user);
        inMemoryUserStorage.create(user2);
        inMemoryUserStorage.create(user3);
        filmService.addLike(film.getId(), user.getId());
        filmService.addLike(film.getId(), user2.getId());
        filmService.addLike(film.getId(), user3.getId());
        filmService.addLike(film2.getId(), user.getId());
        filmService.addLike(film2.getId(), user2.getId());
        filmService.addLike(film3.getId(), user.getId());
        LinkedList<Film> films = new LinkedList<>();
        films.addFirst(film);
        films.addLast(film2);
        films.addLast(film3);
        assertEquals(films, controller.popularFilms(3));
    }
}