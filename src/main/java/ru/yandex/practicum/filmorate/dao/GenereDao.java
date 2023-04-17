package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Genere;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenereDao {

    private final JdbcTemplate jdbcTemplate;

    public Collection<Genere> getAllGeners() {
        String sql = "SELECT * FROM Genere";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenere(rs));
    }

    public Genere getGenereById(int id) {
        String sql = "SELECT * FROM GENERE WHERE genere_id = ?";

        try {
            Genere genere = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeGenere(rs), id);
            log.debug("Найден жанр {}", genere);
            return genere;
        } catch (EmptyResultDataAccessException e) {
            log.debug("Жанр с id = {} не найден", id);
            throw new DataNotFoundException(String.format("Жанр с id = %d не найден", id));
        }
    }

    public Genere makeGenere(ResultSet rs) throws SQLException {
        int genereId = rs.getInt("genere_id");
        String name = rs.getString("name");
        return Genere.builder()
                .id(genereId)
                .name(name)
                .build();
    }
}
