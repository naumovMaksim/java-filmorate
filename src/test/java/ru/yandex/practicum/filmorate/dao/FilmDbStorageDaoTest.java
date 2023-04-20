package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageDaoTest {
    Film film;
    Film film2;
    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorageDao filmDbStorageDao;

    @BeforeEach
    void beforeEach() throws IOException {
        jdbcTemplate.update(Files.readString(Paths.get("src/test/java/resources/schema1.sql")));
        jdbcTemplate.update(Files.readString(Paths.get("src/test/java/resources/data1.sql")));
        film = Film.builder()
                .name("Титаник")
                .description("Фильм про корабль")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(90)
                .rate(1)
                .mpa(new Mpa(1, "G"))
                .genres(new LinkedHashSet<>())
                .build();
        film2 = Film.builder()
                .name("Кот в сапогах")
                .description("Фильм про кота")
                .releaseDate(LocalDate.of(2011, 1, 1))
                .duration(80)
                .rate(2)
                .mpa(new Mpa(1, "G"))
                .genres(new LinkedHashSet<>())
                .build();
    }

    @Test
    void findFilm() {
        filmDbStorageDao.create(film);
        assertEquals(film, filmDbStorageDao.findFilm(1));
    }

    @Test
    void findAll() {
        filmDbStorageDao.create(film);
        filmDbStorageDao.create(film2);
        assertEquals(List.of(film, film2), filmDbStorageDao.findAll());
    }

    @Test
    void create() {
        filmDbStorageDao.create(film);
        assertEquals(film, filmDbStorageDao.findFilm(1));
    }

    @Test
    void update() {
        filmDbStorageDao.create(film);
        Film film1 = Film.builder()
                .id(1)
                .name("Кот в сапогах")
                .description("Фильм про кота")
                .releaseDate(LocalDate.of(2011, 1, 1))
                .duration(80)
                .rate(2)
                .mpa(new Mpa(1, "G"))
                .genres(new LinkedHashSet<>())
                .build();
        filmDbStorageDao.update(Film.builder()
                .id(1)
                .name("Кот в сапогах")
                .description("Фильм про кота")
                .releaseDate(LocalDate.of(2011, 1, 1))
                .duration(80)
                .rate(2)
                .mpa(new Mpa(1, "G"))
                .genres(new LinkedHashSet<>())
                .build());

        assertEquals(film1, filmDbStorageDao.findFilm(1));
    }
}