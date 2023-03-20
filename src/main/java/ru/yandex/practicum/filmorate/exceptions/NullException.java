package ru.yandex.practicum.filmorate.exceptions;

public class NullException extends RuntimeException {
    private final String parameter;

    public NullException(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
