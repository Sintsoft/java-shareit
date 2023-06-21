package ru.practicum.shareit.utility.errorHandling.exceptions;

public class ShareItNotAllowedAction extends RuntimeException {

    public ShareItNotAllowedAction(String message) {
        super(message);
    }
}
