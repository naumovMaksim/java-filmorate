package ru.yandex.practicum.filmorate.model;


import lombok.*;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Genere {

    private int id;
    private String name;

    public Genere(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Genere makeGenere(ResultSet rs) throws SQLException {
        int genereId = rs.getInt("genere_id");
        String name = rs.getString("NAME");

        return Genere.builder()
                .id(genereId)
                .name(name)
                .build();
    }
}
