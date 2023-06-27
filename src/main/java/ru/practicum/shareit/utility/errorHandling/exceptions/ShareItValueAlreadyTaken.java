package ru.practicum.shareit.utility.errorHandling.exceptions;

public class ShareItValueAlreadyTaken extends RuntimeException {

    public ShareItValueAlreadyTaken(String message) {
        super(message);
    }
}
