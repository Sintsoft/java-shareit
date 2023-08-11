package ru.practicum.shareit.utility.validation;

import ru.practicum.shareit.utility.exceptions.ShareItInvalidEntity;

public abstract class RequestParameterValidation {
    public static void paginationParameterValidation(int from, int size) {
        if (from < 0) {
            throw new ShareItInvalidEntity("From can't be negative.");
        } else if (size < 1) {
            throw new ShareItInvalidEntity("Size can be only positive.");
        }

    }
}
