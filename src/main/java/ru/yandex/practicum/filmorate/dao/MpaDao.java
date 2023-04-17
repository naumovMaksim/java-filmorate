package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
@Slf4j
public class MpaDao {

    private final JdbcTemplate jdbcTemplate;

    public Collection<Mpa> getAllMpa() {
        String sql = "SELECT * FROM MPA";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
    }

    public Mpa getMpaById(int id) {
        String sql = "SELECT * FROM MPA WHERE MPA_ID = ?";

        try {
            Mpa mpa = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeMpa(rs), id);
            log.debug("Найден рейтинг: {}", mpa);
            return mpa;
        } catch (EmptyResultDataAccessException e) {
            log.error("Рейтинг с id = {} не найден", id);
            throw new DataNotFoundException(String.format("Рейтинг с id = %d не найден", id));
        }
    }

    public Mpa makeMpa(ResultSet rs) throws SQLException {
        int ratingId = rs.getInt("mpa_id");
        String name = rs.getString("name");
        return Mpa.builder()
                .id(ratingId)
                .name(name)
                .build();
    }
}
