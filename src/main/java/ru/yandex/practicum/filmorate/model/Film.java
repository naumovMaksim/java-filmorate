package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    private Set<Integer> usersLikes = new HashSet<>();

    public Film(@Valid int id,@Valid String name,@Valid String description,@Valid LocalDate releaseDate,@Valid int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.usersLikes = new HashSet<>();
    }

    public void setUsersLikes(User user) {
        usersLikes.add(user.getId());
    }

    public void deleteUsersLikes(User user) {
        usersLikes.remove(user.getId());
    }
}
