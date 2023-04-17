package ru.yandex.practicum.filmorate.model;


import lombok.*;

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
}
