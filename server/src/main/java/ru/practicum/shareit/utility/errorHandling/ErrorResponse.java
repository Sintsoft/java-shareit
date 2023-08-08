package ru.practicum.shareit.utility.errorHandling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private final String error;

    private String exception;
}
