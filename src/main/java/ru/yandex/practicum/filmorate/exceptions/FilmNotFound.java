package ru.yandex.practicum.filmorate.exceptions;

public class FilmNotFound extends RuntimeException {
    public FilmNotFound(String s) {
        super(s);
    }
}
