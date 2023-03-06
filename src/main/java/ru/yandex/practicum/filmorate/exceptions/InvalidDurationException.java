package ru.yandex.practicum.filmorate.exceptions;

public class InvalidDurationException extends RuntimeException {
    public InvalidDurationException(String s) {
        super(s);
    }
}
