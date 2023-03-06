package ru.yandex.practicum.filmorate.exceptions;

public class InvalidReleaseDateException extends RuntimeException {
    public InvalidReleaseDateException(String s) {
        super(s);
    }
}
