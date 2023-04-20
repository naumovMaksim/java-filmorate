package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genere;
import ru.yandex.practicum.filmorate.storage.film.FilmGenreService;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmGenreDao implements FilmGenreService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public LinkedHashSet<Genere> getFilmGenre(int filmId) {
        String sql = "SELECT g.genere_id, g.name FROM GENRE_FILM AS fg " +
                "LEFT JOIN GENERE AS g ON fg.genere_id = g.genere_id WHERE fg.film_id = ?";
        Collection<Genere> generes = jdbcTemplate.query(sql, (rs, rowNum) -> Genere.makeGenere(rs), filmId);
        return new LinkedHashSet<>(generes);
    }

    @Override
    public void addGenreToFilm(Film film) {
        String sqlGener = "INSERT INTO GENRE_FILM (GENERE_ID, film_id) VALUES (?,?)";//я так и не разобрался как мне добавить что фильму принадлежит определенный жанр в таблице GENRE_FILM без этого метода, а для работы с жанрами у меня таблица Genere и GenreDao

        LinkedHashSet<Genere> geners = film.getGenres();

        for (Genere genre : geners) {
            jdbcTemplate.update(sqlGener, genre.getId(), film.getId());
        }
    }

    @Override
    public void deleteAllFilmGenres(Film film) {
        String sql = "DELETE FROM GENRE_FILM WHERE FILM_ID = ?";

        jdbcTemplate.update(sql, film.getId());
    }
}
