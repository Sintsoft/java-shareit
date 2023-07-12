package ru.practicum.shareit.utility.validation;

import ru.practicum.shareit.utility.exceptions.ShareItIvanlidEntity;

public abstract class RequestParameterValidation {

    public static void paginationParameterValidation(int from, int size) {
        if (from < 0) {
            throw new ShareItIvanlidEntity("From can't be negative.");
        } else if (size < 1) {
            throw new ShareItIvanlidEntity("Size can be only positive.");
        }

    }
}
