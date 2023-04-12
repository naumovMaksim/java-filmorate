package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Genere implements Comparable<Genere> {
    private int id;
    private String name;

    @Override
    public int compareTo(Genere o) {
        return this.id - o.getId();
    }
}
