package ru.yandex.practicum.filmorate.validators;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
@Slf4j
public class FilmValidator {
    private static final int MAX_DESCRIPTION_LENGTH = 200;

    public void filmValidator(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Название не может быть пустым.");
            throw new InvalidNameException("Название не может быть пустым.");
        }
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.warn("Максимальная длина описания — 200 символов.");
            throw new InvalidDescriptionException("Максимальная длина описания — 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.warn("Дата релиза — не раньше 28.12.1895.");
            throw new InvalidReleaseDateException("Дата релиза — не раньше 28.12.1895.");
        }
        if (film.getDuration() < 1) {
            log.warn("Продолжительность фильма должна быть положительной.");
            throw new InvalidDurationException("Продолжительность фильма должна быть положительной.");
        }
    }
}
