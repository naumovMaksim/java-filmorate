package ru.yandex.practicum.filmorate.validators;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.time.Month;

@Slf4j
@Component
public class FilmValidator {
    private static final int MAX_DESCRIPTION_LENGTH = 200;

    public void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Название не может быть пустым.");
            throw new ValidationException("Название не может быть пустым.");
        }
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.error("Максимальная длина описания — 200 символов.");
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.error("Дата релиза — не раньше 28.12.1895.");
            throw new ValidationException("Дата релиза — не раньше 28.12.1895.");
        }
        if (film.getDuration() < 1) {
            log.error("Продолжительность фильма должна быть положительной.");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }
}
