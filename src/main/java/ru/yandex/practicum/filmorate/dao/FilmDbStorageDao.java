package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmGenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
@Qualifier("FilmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorageDao implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmGenreService filmGenreService;
    private final MpaService mpaService;

    @Override
    public Film findFilm(int id) {
        String sql = "SELECT * FROM FILMS WHERE FILM_ID = ?";

        try {
            Film film = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFilm(rs), id);
            assert film != null;
            film.setGenres(filmGenreService.getFilmGenre(id));
            log.debug("Найден фильм: {}", film);
            return film;
        } catch (EmptyResultDataAccessException e) {
            log.error("Фильм с id = {} не найден", id);
            throw new DataNotFoundException(String.format("Фильм с id = %d не найден", id));
        }
    }

    @Override
    public Collection<Film> findAll() {
        String sql = "SELECT * FROM FILMS";//тут тоже не понял как мне получить жанры одним запросом вместе с фильмом, если делать через left join, то в таблице фильмы дублируются, потому что одному фильму может соответстовать несколько жанров
        Collection<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        for (Film film : films) {
            film.setGenres(filmGenreService.getFilmGenre(film.getId()));
        }
        return films;
    }

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO FILMS (name, description, release_date, duration, rate, mpa) VALUES (?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getRate());
            stmt.setInt(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        int idKey = Objects.requireNonNull(keyHolder.getKey()).intValue();
        film.setId(idKey);
        if (film.getGenres() != null) {
            filmGenreService.addGenreToFilm(film);
        }
        return film;
    }

    @Override
    public void update(Film film) {
        String sql = "UPDATE FILMS SET name = ?, description = ?, release_date = ?" +
                ", duration = ?, rate = ?, mpa = ? WHERE film_id = ?";

        int update = jdbcTemplate.update(sql, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getRate(), film.getMpa().getId(), film.getId());
        if (update == 0) {
            log.error("Фильм с id = {} не найден", film.getId());
            throw new DataNotFoundException(String.format("Фильм с id = %d не найден", film.getId()));
        }
        if (film.getGenres() != null) {
            filmGenreService.deleteAllFilmGenres(film);
            filmGenreService.addGenreToFilm(film);
        }
    }

    @Override
    public Collection<Film> getPopularFilms(Integer limit) {
        String sql = "SELECT f.FILM_ID, f.Name, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.RATE, f.MPA " +
                "FROM FILMS AS f " +
                "LEFT JOIN FILM_LIKES AS fl ON f.FILM_ID = fl.FILM_ID " +
                "GROUP BY f.FILM_ID ORDER BY COUNT(fl.FILM_ID) desc LIMIT ?";
        Collection<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), limit);
        films.forEach(film -> film.setGenres(filmGenreService.getFilmGenre(film.getId())));
        return films;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate date = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        int ratingId = rs.getInt("rate");
        Mpa mpa = new Mpa(rs.getInt("mpa"), mpaService.getMpaById(rs.getInt("mpa")).getName());
        return new Film(id, name, description, date, duration, ratingId, mpa);
    }
}
