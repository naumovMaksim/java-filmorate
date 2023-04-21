package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.LikesDao;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmControllerTest {
    private final JdbcTemplate jdbcTemplate;
    private Film film;
    private User user;
    private final FilmController controller;
    private final UserDbStorage userDbStorage;
    private final LikesDao likesDao;

    @BeforeEach
    void beforeEach() throws IOException {
        jdbcTemplate.update(Files.readString(Paths.get("src/test/java/resources/schema1.sql")));
        jdbcTemplate.update(Files.readString(Paths.get("src/test/java/resources/data1.sql")));
        film = Film.builder()
                .name("Титаник")
                .description("Фильм про лодку")
                .releaseDate(LocalDate.of(1998, Month.FEBRUARY, 20))
                .duration(196)
                .rate(1)
                .mpa(new Mpa(1, "G"))
                .genres(new LinkedHashSet<>())
                .build();
        user = User.builder()
                .email("Santa@mail.ru")
                .login("Santa")
                .name("Санта")
                .birthday(LocalDate.of(2000, Month.DECEMBER, 6))
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
        assertEquals("Фильм с id = 0 не найден", exception.getParameter());
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
        assertEquals(film, controller.findFilm(1));
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
                .name("Кот в сапогах")
                .description("Фильм про кота")
                .releaseDate(LocalDate.of(1998, Month.FEBRUARY, 20))
                .duration(196)
                .rate(1)
                .mpa(new Mpa(1, "G"))
                .genres(new LinkedHashSet<>())
                .build();

        controller.create(film);
        controller.update(film1);
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
                () -> controller.update(film1));
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
                () -> controller.update(film1));
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
                () -> controller.update(film1));
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
                () -> controller.update(film1));
        assertEquals("Продолжительность фильма должна быть положительной.", exception.getParameter());
    }

    @Test
    void addLike() {
        controller.create(film);
        userDbStorage.create(user);
        controller.addLike(film.getId(), user.getId());
        assertEquals(List.of(user), likesDao.getFilmLikes(film.getId()));
    }

    @Test
    void addLikeWithWrongFilmData() {
        controller.create(film);
        userDbStorage.create(user);
        final DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> controller.addLike(0, user.getId()));
        assertEquals("Фильм с id = 0 или пользоватль с id = 1 не найдены", exception.getParameter());
    }

    @Test
    void addLikeWithWrongUserData() {
        controller.create(film);
        userDbStorage.create(user);
        final DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> controller.addLike(film.getId(), 0));
        assertEquals("Пользователь с id = 0 не найден", exception.getParameter());
    }

    @Test
    void deleteLike() {
        controller.create(film);
        userDbStorage.create(user);
        controller.addLike(film.getId(), user.getId());
        assertTrue(likesDao.getFilmLikes(film.getId()).contains(user));
        controller.deleteLike(film.getId(), user.getId());
        assertFalse(likesDao.getFilmLikes(film.getId()).contains(user));
    }

    @Test
    void deleteLikeWithWrongFilmData() {
        controller.create(film);
        userDbStorage.create(user);
        controller.addLike(film.getId(), user.getId());
        assertTrue(likesDao.getFilmLikes(film.getId()).contains(user));
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> controller.deleteLike(0, user.getId()));
        assertTrue(likesDao.getFilmLikes(film.getId()).contains(user));
        assertEquals("Вы уже убрали лайк с фильма id = 0", exception.getParameter());
    }

    @Test
    void deleteLikeWithWrongUserData() {
        controller.create(film);
        userDbStorage.create(user);
        controller.addLike(film.getId(), user.getId());
        assertTrue(likesDao.getFilmLikes(film.getId()).contains(user));
        final DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> controller.deleteLike(film.getId(), 0));
        assertTrue(likesDao.getFilmLikes(film.getId()).contains(user));
        assertEquals("Пользователь с id = 0 не найден", exception.getParameter());
    }

    @Test
    void popularFilms() {
        Film film2 = Film.builder()
                .name("Тит")
                .description("Фильм про лодку")
                .releaseDate(LocalDate.of(1998, Month.FEBRUARY, 20))
                .duration(196)
                .rate(1)
                .mpa(new Mpa(1, "G"))
                .genres(new LinkedHashSet<>())
                .build();

        Film film3 = Film.builder()
                .name("Тита")
                .description("Фильм про лодку")
                .releaseDate(LocalDate.of(1998, Month.FEBRUARY, 20))
                .duration(196)
                .rate(1)
                .mpa(new Mpa(1, "G"))
                .genres(new LinkedHashSet<>())
                .build();

        User user2 = User.builder()
                .email("Sta@mail.ru")
                .login("Santa")
                .name("Санта")
                .birthday(LocalDate.of(2000, Month.DECEMBER, 6))
                .build();
        User user3 = User.builder()
                .id(1)
                .email("S@mail.ru")
                .login("Santa")
                .name("Санта")
                .birthday(LocalDate.of(0, Month.DECEMBER, 6))
                .build();
        controller.create(film);
        controller.create(film2);
        controller.create(film3);
        userDbStorage.create(user);
        userDbStorage.create(user2);
        userDbStorage.create(user3);
        controller.addLike(film.getId(), user.getId());
        controller.addLike(film.getId(), user2.getId());
        controller.addLike(film.getId(), user3.getId());
        controller.addLike(film2.getId(), user.getId());
        controller.addLike(film2.getId(), user2.getId());
        controller.addLike(film3.getId(), user.getId());
        LinkedList<Film> films = new LinkedList<>();
        films.addFirst(film);
        films.addLast(film2);
        films.addLast(film3);
        assertEquals(films, controller.popularFilms(3));
    }
}