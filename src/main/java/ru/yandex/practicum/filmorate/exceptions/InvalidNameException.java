package ru.yandex.practicum.filmorate.exceptions;

public class InvalidNameException extends RuntimeException {
    public InvalidNameException(String s) {
        super(s);
    }
}
