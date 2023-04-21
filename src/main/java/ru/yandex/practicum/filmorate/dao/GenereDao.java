package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Genere;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenereDao implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genere> getAllGeners() {
        String sql = "SELECT * FROM Genere";

        return jdbcTemplate.query(sql, (rs, rowNum) -> Genere.makeGenere(rs));
    }

    @Override
    public Genere getGenereById(int id) {
        String sql = "SELECT * FROM GENERE WHERE genere_id = ?";

        try {
            Genere genere = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> Genere.makeGenere(rs), id);
            log.debug("Найден жанр {}", genere);
            return genere;
        } catch (EmptyResultDataAccessException e) {
            log.debug("Жанр с id = {} не найден", id);
            throw new DataNotFoundException(String.format("Жанр с id = %d не найден", id));
        }
    }
}
