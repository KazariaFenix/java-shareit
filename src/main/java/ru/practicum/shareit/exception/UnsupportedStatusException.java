package ru.practicum.shareit.exception;

public class UnsupportedStatusException extends RuntimeException {

    public UnsupportedStatusException(String errorMessage) {
        super(errorMessage);
    }
}
