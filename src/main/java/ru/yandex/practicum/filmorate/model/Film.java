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
    private int rating;
    @JsonIgnore
    private Set<Integer> usersLikes = new HashSet<>();
    @JsonIgnore
    private Set<Genere> genere = new TreeSet<>();

    public Film(@Valid int id, @Valid String name, @Valid String description, @Valid LocalDate releaseDate
            , @Valid int duration, @Valid int rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rating = rating;
        this.usersLikes = new HashSet<>();
        this.genere = new HashSet<>();
    }

    public void setUsersLikes(User user) {
        usersLikes.add(user.getId());
    }

    public void deleteUsersLikes(User user) {
        usersLikes.remove(user.getId());
    }

    public void setGenere(Genere g) {
        genere.add(g);
    }
}
