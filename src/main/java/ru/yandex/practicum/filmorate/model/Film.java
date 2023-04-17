package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    @NotNull
    private int id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Min(1)
    private int duration;
    @NotNull
    private int rate;
    private Mpa mpa;
    @JsonIgnore
    private Set<Integer> usersLikes = new HashSet<>();
    @JsonIgnore
    private LinkedHashSet<Genere> genres = new LinkedHashSet<>();

    public Film(@Valid int id, @Valid String name, @Valid String description, @Valid LocalDate releaseDate
            , @Valid int duration, @Valid int rating, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rating;
        this.mpa = mpa;
        this.genres = new LinkedHashSet<>();
    }
}
