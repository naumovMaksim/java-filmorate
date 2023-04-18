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
import ru.yandex.practicum.filmorate.model.User;
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
    private final LikesDao likesDao;
    private final FilmGenreDao filmGenreDao;
    private final MpaDao mpaDao;

    @Override
    public Film findFilm(int id) {
        String sql = "SELECT * FROM FILMS WHERE film_id = ?";

        try {
            Film film = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFilm(rs), id);
            assert film != null;
            filmGenreDao.addGenreToFilm(film);
            film.setUsersLikes(likesDao.getFilmLikesUserIds(id));
            film.setGenres(filmGenreDao.getFilmGenre(id));
            log.debug("Найден фильм: {}", film);
                return film;
        } catch (EmptyResultDataAccessException e) {
            log.error("Фильм с id = {} не найден", id);
            throw new DataNotFoundException(String.format("Фильм с id = %d не найден", id));
        }
    }

    @Override
    public Collection<Film> findAll() {
        String sql = "SELECT * FROM FILMS";
        Collection<Film> films = jdbcTemplate.query(sql,  (rs, rowNum) -> makeFilm(rs));
        for (Film film : films) {
            filmGenreDao.addGenreToFilm(film);
            film.setUsersLikes(likesDao.getFilmLikesUserIds(film.getId()));
            film.setGenres(filmGenreDao.getFilmGenre(film.getId()));
        }
        return films;
    }

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO FILMS (name, description, release_date, duration, rating_id, mpa) VALUES (?,?,?,?,?,?)";

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
            filmGenreDao.addGenreToFilm(film);
        }
        return film;
        }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE FILMS SET name = ?, description = ?, release_date = ?" +
                ", duration = ?, rating_id = ?, mpa = ? WHERE film_id = ?";

        int update = jdbcTemplate.update(sql, film.getName(), film.getDescription()
                , film.getReleaseDate(), film.getDuration(), film.getRate(), film.getMpa().getId(), film.getId());
        if (update == 0) {
            log.error("Фильм с id = {} не найден", film.getId());
            throw new DataNotFoundException(String.format("Фильм с id = %d не найден", film.getId()));
        }
        if (film.getGenres() != null) {
            filmGenreDao.deleteAllFilmGenres(film);
            filmGenreDao.addGenreToFilm(film);
            film.setGenres(filmGenreDao.getFilmGenre(film.getId()));
        }
        Collection<User> likes = likesDao.getFilmLikes(film.getId());
        HashSet<Integer> setLikes = new HashSet<>();
        for (User user : likes) {
            setLikes.add(user.getId());
        }
        film.setUsersLikes(setLikes);
        return film;
    }

    @Override
    public Collection<Film> getPopularFilms(Integer limit) {
        String sql = "SELECT f.FILM_ID, f.Name, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.RATING_ID, f.MPA " +
                "FROM FILMS AS f " +
                "LEFT JOIN FILM_LIKES AS fl ON f.FILM_ID = fl.FILM_ID " +
                "GROUP BY f.FILM_ID ORDER BY COUNT(fl.FILM_ID) desc LIMIT ?";
        Collection<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), limit);
        films.forEach(film -> {
            film.setGenres(filmGenreDao.getFilmGenre(film.getId()));
            film.setUsersLikes(likesDao.getFilmLikesUserIds(film.getId()));
        });
        return films;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate date = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        int ratingId = rs.getInt("rating_id");
        Mpa mpa = new Mpa(rs.getInt("mpa"), mpaDao.getMpaById(rs.getInt("mpa")).getName());
        return new Film(id, name, description, date, duration, ratingId, mpa);
    }
}
