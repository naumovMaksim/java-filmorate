package ru.yandex.practicum.filmorate.model;

public class ErrorResponse {
    String error;

    public String getError() {
        return error;
    }

    public ErrorResponse(String error) {
        this.error = error;
    }
}
