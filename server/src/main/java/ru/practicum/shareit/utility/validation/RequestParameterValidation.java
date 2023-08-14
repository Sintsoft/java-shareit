package ru.practicum.shareit.utility.validation;

import ru.practicum.shareit.utility.exceptions.ShareItInvalidEntity;

public class RequestParameterValidation {

    private RequestParameterValidation() {
    }

    public static void paginationParameterValidation(int from, int size) {
        if (from < 0 || size < 1) {
            throw new ShareItInvalidEntity("Invalid pagination parameters");
        }
    }
}
