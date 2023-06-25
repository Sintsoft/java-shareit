package ru.practicum.shareit.utility.exception;

public class UnpermittedAction extends RuntimeException {

    public UnpermittedAction(String message) {
        super(message);
    }
}
