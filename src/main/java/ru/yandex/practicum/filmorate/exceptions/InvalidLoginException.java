package ru.yandex.practicum.filmorate.exceptions;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException (String s) {
        super(s);
    }
}
