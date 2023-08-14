package ru.practicum.shareit.utility.errorHandling;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

    public ErrorResponse(String error) {
        this.error = error;
    }

    private String error;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String exception;
}
